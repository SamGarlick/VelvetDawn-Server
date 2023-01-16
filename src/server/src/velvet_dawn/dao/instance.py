import redis
from typing import Optional, List
from velvet_dawn.config import Config


# TODO Document what this is and how it works
# TODO Test running unit tests utilising redis


redis_connection: Optional[redis.Redis] = None
_data = {}


def instantiate(config: Config):
    pass


def clear():
    global _data

    if redis_connection:
        redis_connection.flushall()
    _data = {}


def set_redis():
    # TODO
    pass


def save():
    # todo only save on begin turns and such
    pass


def load():
    # todo
    pass


def set_value(key: str, value: str):
    if redis_connection:
        redis_connection.set(key, value)
    else:
        _data[key] = value


def get_value(key, default=None):
    if redis_connection:
        return redis_connection.get(key) or default
    return _data.get(key, default)


def rem(key: str):
    if redis_connection:
        return redis_connection.delete(key, )
    elif key in _data:
        del _data[key]

"""
Set Operations
"""


def sadd(name: str, values: List[str]):
    if redis_connection:
        redis_connection.sadd(name, *values)
    else:
        if name not in _data:
            _data[name] = set()
        _data[name].update(values)


def srem(name: str, values: List[str]):
    if redis_connection:
        redis_connection.srem(name, *values)
    else:
        if name in _data and isinstance(_data[name], set):
            for value in values:
                if name in _data and value in _data[name]:
                    _data[name].remove(value)

            if not _data[name]:
                del _data[name]


def smembers(name):
    if redis_connection:
        redis_connection.smembers(name)
    else:
        if name in _data and isinstance(_data[name], set):
            return _data[name]
    return set()


def sismember(name, value):
    if redis_connection:
        return redis_connection.sismember(name, value)

    if name in _data and isinstance(_data[name], set):
        return value in _data[name]

    return False