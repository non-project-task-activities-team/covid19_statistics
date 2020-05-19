The API uses RSocket protocol, for data streaming.

It streams specific data from Mongo DB collections using Mongo Change Events. 
Mongo DB collections streaming works only with a replica set DB instance(s).

**To launch the Mongo DB replica set instances use following commands:**

Launches primary replica:
```
mongod --dbpath "D:\mongodb_rs1" --bind_ip localhost --port 30000 --replSet "rs0"
```
To connect to primary replica use "&readPreference=primary"

Launches additional replica:
```
mongod --dbpath "D:\mongodb_rs2" --bind_ip localhost --port 40000 --replSet "rs0"
```
To connect to this replica use "&readPreference=primaryPreferred"

Connect to primary replica via terminal:
```
mongo --port 30000
```

Run following commands in mongo shell to setup replicas config:
```
var replicaSetConfig = { 
  _id: "rs0", 
  members: [
    {
      _id: 0, 
      host: 'localhost:30000', 
      priority: 10
    },
    {
      _id: 1, 
      host: 'localhost:40000'
    }
  ] 
};

rs.initiate(replicaSetConfig);
```

Run following commands in mongo shell to create user with administrator permissions:
```
use admin
db.createUser({user: "root", pwd: "root",roles: [ "userAdminAnyDatabase", "dbAdminAnyDatabase", "readWriteAnyDatabase" ]})
```
