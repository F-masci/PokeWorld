import os, sqlite3, requests, json
import threading, queue

_DB = os.path.join('app', 'src', 'main', 'assets', 'pokemon.db')
_API = 'https://pokeapi.co/api/v2/'
_NUM_THREAD = 20

_ABILITY_LOADED = True
_ITEM_LOADED = True
_MOVE_LOADED = True
_POKEMON_LOADED = False

connection = sqlite3.connect(_DB, check_same_thread=False)

abilityMap = {}
itemMap = {}
moveMap = {}

pokemonQueue = queue.Queue()
abilityQueue = queue.Queue()
itemQueue = queue.Queue()
moveQueue = queue.Queue()

## ------ UTILS ------ ##


def camel_case(text):
    words = text.split("-")
    return words[0].lower() + "".join(word.capitalize() for word in words[1:])


def format_multi_language_resources(array, text_field):
    array = reversed(array)
    res = []
    languages = set()
    for t in array:
        language = t.get('language').get('name').replace('\n', ' ')
        text = t.get(text_field)
        if language in languages: continue
        languages.add(language)
        res.append({"language": language, "text": text})

    return res

## ------------------- ##

def insert_pokemon(threadId):

    cursor = connection.cursor()

    while True:

        try:

            url = pokemonQueue.get(block=False)
            pDetail = requests.get(url).json()

            stats = pDetail.get('stats')
            for s in stats:
                nameStat = s.get('stat').get('name')
                valStat = s.get('base_stat')
                pDetail[camel_case(nameStat)] = valStat

            types = pDetail.get('types')
            pDetail['type1'] = None
            pDetail['type2'] = None
            for t in types:
                nameType = t.get('type').get('name')
                ordType = t.get('slot')
                pDetail['type' + str(ordType)] = nameType

            id = pDetail.get('id')
            name = pDetail.get('name')
            height = pDetail.get('height')
            weight = pDetail.get('weight')

            hp = pDetail.get('hp')
            attack = pDetail.get('attack')
            defense = pDetail.get('defense')
            specialAttack = pDetail.get('specialAttack')
            specialDefense = pDetail.get('specialDefense')
            speed = pDetail.get('speed')

            type1 = pDetail.get('type1')
            type2 = pDetail.get('type2')

            spriteDefault = pDetail.get('sprites').get('front_default')
            spriteShiny = pDetail.get('sprites').get('front_shiny')

            print(f"[{threadId}-Pokemon] Inserting pokemon {name}: remaining {pokemonQueue.qsize()}")

            retry = True
            while retry:
                try:
                    cursor.execute('''
                    INSERT INTO pokemon(id, name, height, weight, hp, attack, defense, specialAttack, specialDefense, speed, type1, type2, spriteDefault, spriteShiny)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                    ''', (id, name, height, weight, hp, attack, defense, specialAttack, specialDefense, speed, type1, type2, spriteDefault, spriteShiny))
                    retry = False
                except sqlite3.OperationalError:
                    retry = True
                except:
                    retry = False

            abilities = pDetail.get('abilities')
            for a in abilities:
                abilityId = abilityMap.get(a.get('ability').get('name'), None)
                if abilityId is None:
                    print(f"[{threadId}-Pokemon] Ability id {abilityId} not linked")
                    continue
                cursor.execute('''
                INSERT INTO pokemon_ability(pokemonId, abilityId)
                VALUES (?, ?);
                ''', (id, abilityId))

            items = pDetail.get('held_items')
            for i in items:
                itemId = itemMap.get(i.get('item').get('name'), None)
                if itemId is None:
                    print(f"[{threadId}-Pokemon] Item id {itemId} not linked")
                    continue
                cursor.execute('''
                INSERT INTO pokemon_item(pokemonId, itemId)
                VALUES (?, ?);
                ''', (id, itemId))

            moves = pDetail.get('moves')
            for m in moves:
                moveId = moveMap.get(m.get('move').get('name'), None)
                if moveId is None:
                    print(f"[{threadId}-Pokemon] Move id {moveId} not linked")
                    continue
                cursor.execute('''
                INSERT INTO pokemon_move(pokemonId, moveId)
                VALUES (?, ?);
                ''', (id, moveId))

        except queue.Empty:
            print(f"[{threadId}-Pokemon] Terminated")
            break


def insert_abilities(threadId):

    cursor = connection.cursor()

    while True:

        try:
            url = abilityQueue.get(block=False)
            aDetail = requests.get(url).json()

            id = aDetail.get('id')
            name = aDetail.get('name', '-')
            isMainSeries = aDetail.get('is_main_series')

            print(f"[{threadId}-Ability] Inserting ability {name}: remaining {abilityQueue.qsize()}")

            abilityMap[name] = id

            if not _ABILITY_LOADED:

                names = format_multi_language_resources(aDetail.get('names', []), 'name')
                effects = format_multi_language_resources(aDetail.get('flavor_text_entries', []), 'flavor_text')

                cursor.execute('''
                INSERT INTO ability (id, name, isMainSeries, names, effects)
                VALUES (?, ?, ?, ?, ?);
                ''', (id, name, isMainSeries, json.dumps(names), json.dumps(effects)))

            abilityQueue.task_done()

        except queue.Empty:
            print(f"[{threadId}-Ability] Terminated")
            break

def insert_items(threadId):

    cursor = connection.cursor()

    while True:

        try:

            url = itemQueue.get(block=False)
            iDetail = requests.get(url).json()

            id = iDetail.get('id')
            name = iDetail.get('name', '-')
            cost = iDetail.get('cost')
            sprite = iDetail.get('sprites').get('default')

            print(f"[{threadId}-Item] Inserting item {name}: remaining {itemQueue.qsize()}")

            itemMap[name] = id

            if not _ITEM_LOADED:

                names = format_multi_language_resources(iDetail.get('names', []), 'name')
                effects = format_multi_language_resources(iDetail.get('flavor_text_entries', []), 'text')

                cursor.execute('''
                INSERT INTO item (id, name, cost, sprite, names, effects)
                VALUES (?, ?, ?, ?, ?, ?);
                ''', (id, name, cost, sprite, json.dumps(names), json.dumps(effects)))

            itemQueue.task_done()
        
        except queue.Empty:
            print(f"[{threadId}-Item] Terminated")
            break

def insert_moves(threadId):

    cursor = connection.cursor()

    while True:

        try:

            url = moveQueue.get(block=False)
            mDetail = requests.get(url).json()

            id = mDetail.get('id')
            name = mDetail.get('name')
            accuracy = mDetail.get('accuracy')
            effectChance = mDetail.get('effect_chance')
            power = mDetail.get('power')
            pp = mDetail.get('pp')
            priority = mDetail.get('priority')
            type = mDetail.get('type').get('name')

            print(f"[{threadId}-Move] Inserting move {name}: remaining {moveQueue.qsize()}")

            moveMap[name] = id

            if not _MOVE_LOADED:

                names = format_multi_language_resources(mDetail.get('names', []), 'name')
                effects = format_multi_language_resources(mDetail.get('flavor_text_entries', []), 'flavor_text')

                cursor.execute('''
                INSERT INTO move (id, name, accuracy, effectChance, power, pp, priority, type, names, descriptions)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                ''', (id, name, accuracy, effectChance, power, pp, priority, type, json.dumps(names), json.dumps(effects)))

            moveQueue.task_done()

        except queue.Empty:
            print(f"[{threadId}-Move] Terminated")
            break


if __name__ == '__main__':

    ###########
    # ABILITY #
    ###########

    abilityThreads = []
    
    aCounter = requests.get(_API + 'ability').json().get('count', 0)
    print(f"[MAIN] Ability counter: {aCounter}")
    
    aList = requests.get(_API + 'ability/?limit=' + str(aCounter) + '&offset=0').json().get('results')
    print("[MAIN] Abilities list loaded")

    for a in aList:
        url = a.get('url', None)
        if url is not None:
            abilityQueue.put(url)

    print("[MAIN] Abilities queue loaded")

    for i in range(_NUM_THREAD):
        t = threading.Thread(target=insert_abilities, args=(i, ))
        abilityThreads.append(t)
        
    print("[MAIN] Ability threads loaded")

    ########
    # ITEM #
    ########

    itemThreads = []

    iCounter = requests.get(_API + 'item').json().get('count', 0)
    print(f"[MAIN] Item counter: {iCounter}")
    
    iList = requests.get(_API + 'item/?limit=' + str(iCounter) + '&offset=0').json().get('results')
    print("[MAIN] Items list loaded")

    for i in iList:
        url = i.get('url', None)
        if url is not None:
            itemQueue.put(url)

    print("[MAIN] Items queue loaded")

    for i in range(_NUM_THREAD):
        t = threading.Thread(target=insert_items, args=(i, ))
        itemThreads.append(t)

    print("[MAIN] Item threads loaded")

    ########
    # MOVE #
    ########

    moveThreads = []

    mCounter = requests.get(_API + 'move').json().get('count', 0)
    print(f"[MAIN] Item counter: {mCounter}")
    
    mList = requests.get(_API + 'move/?limit=' + str(mCounter) + '&offset=0').json().get('results')
    print("[MAIN] Moves list loaded")

    for m in mList:
        url = m.get('url', None)
        if url is not None:
            moveQueue.put(url)

    print("[MAIN] Moves queue loaded")

    for i in range(_NUM_THREAD):
        t = threading.Thread(target=insert_moves, args=(i, ))
        moveThreads.append(t)

    print("[MAIN] Move threads loaded")   

    for t in abilityThreads: t.start()
    for t in itemThreads: t.start()
    for t in moveThreads: t.start()

    print("[MAIN] Threads start")

    for t in abilityThreads: t.join()
    for t in itemThreads: t.join()
    for t in moveThreads: t.join()

    print("[MAIN] Inserting pokemon")

    ###########
    # POKEMON #
    ###########

    pokemonThreads = []
    if not _POKEMON_LOADED:
        pCounter = requests.get(_API + 'pokemon').json().get('count', 0)
        print(f"[MAIN] Pokemon counter: {pCounter}")
        
        pList = requests.get(_API + 'pokemon/?limit=' + str(pCounter) + '&offset=0').json().get('results')
        print("[MAIN] Pokemon list loaded")

        for p in pList:
            url = p.get('url', None)
            if url is not None:
                pokemonQueue.put(url)

        print("[MAIN] Pokemon queue loaded")

        for i in range(_NUM_THREAD):
            t = threading.Thread(target=insert_pokemon, args=(i, ))
            pokemonThreads.append(t)

        print("[MAIN] Pokemon threads loaded")

    for t in pokemonThreads: t.start()

    print("[MAIN] Threads start")

    for t in pokemonThreads: t.join()

    print("[MAIN] Exiting")

    connection.commit()
    connection.close()
