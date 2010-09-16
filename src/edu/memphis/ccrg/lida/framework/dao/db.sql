create table app.lida (
    ID INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(255)
)

create table app.actionselection (
    ID INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    LIDAID INTEGER,
    SERIALIZEDDATA BLOB
)

create table app.perceptualassociativememory (
    ID INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    LIDAID INTEGER,
    SERIALIZEDDATA BLOB
)

create table app.proceduralmemory (
    ID INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    LIDAID INTEGER,
    SERIALIZEDDATA BLOB
)

create table app.sensorymemory (
    ID INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    LIDAID INTEGER,
    SERIALIZEDDATA BLOB
)

