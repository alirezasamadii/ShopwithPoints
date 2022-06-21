db.createUser(
    {
        user: "dinewithme-mongodb",
        pwd: "dinewithme-mongodb",
        roles: [
            {
                role: "readWrite",
                db: "dinewithme"
            }
        ]
    }
);