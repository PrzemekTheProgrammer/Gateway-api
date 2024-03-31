db.createUser(
        {
            user: "szerszu",
            pwd: "szerszu",
            roles: [
                {
                    role: "readWrite",
                    db: "scans"
                }
            ]
        }
);