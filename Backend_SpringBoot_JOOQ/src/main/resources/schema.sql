DROP TABLE IF EXISTS organization, worker;

CREATE TABLE organization (
                              id SERIAL PRIMARY KEY,
                              name VARCHAR(100) NOT NULL,
                              main_organization_id INTEGER REFERENCES organization(id)
);

CREATE TABLE worker (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        main_worker_id INTEGER,
                        organization_id INTEGER NOT NULL ,

                        FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE RESTRICT,
                        FOREIGN KEY (main_worker_id) REFERENCES worker(id) ON DELETE RESTRICT
);
