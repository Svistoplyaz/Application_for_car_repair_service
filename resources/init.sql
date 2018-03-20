CREATE TABLE IF NOT EXISTS Clients (
  PK_Clients INTEGER PRIMARY KEY NOT NULL,
  Name TEXT(100) NOT NULL,
  Phone TEXT(12)
);

---

CREATE TABLE IF NOT EXISTS Service (
  PK_Service INTEGER PRIMARY KEY NOT NULL,
  Name TEXT(50) NOT NULL 
);

---

CREATE TABLE IF NOT EXISTS Service_price (
  PK_Service_price INTEGER PRIMARY KEY NOT NULL,
  Date INTEGER NOT NULL,
  Price INTEGER NOT NULL,
  PK_Service INTEGER NOT NULL 
    REFERENCES Service(PK_Service)
    ON DELETE CASCADE
);

---

CREATE TABLE IF NOT EXISTS Mark (
  PK_Mark INTEGER PRIMARY KEY NOT NULL,
  Name TEXT(20) NOT NULL
);

---

CREATE TABLE IF NOT EXISTS Model (
  PK_Model INTEGER PRIMARY KEY NOT NULL,
  Name TEXT(40) NOT NULL,
  Year INTEGER NOT NULL,
  PK_Mark INTEGER NOT NULL
    REFERENCES Mark(PK_Mark)
    ON DELETE RESTRICT
);

---

CREATE TABLE IF NOT EXISTS `Order` (
  PK_Order INTEGER PRIMARY KEY NOT NULL,
  PK_Clients INTEGER NOT NULL 
    REFERENCES Clients(PK_Clients)
    ON DELETE RESTRICT,
  Registration_number TEXT(20),
  Start_date INTEGER NOT NULL,
  Finish_date INTEGER,
  PK_Model INTEGER NOT NULL,
  PK_Mark INTEGER NOT NULL,
  Finish_cost INTEGER,
  FOREIGN KEY (PK_Model, PK_Mark) 
    REFERENCES Model(PK_Model, PK_Mark)
    ON DELETE RESTRICT
);

---

CREATE TABLE IF NOT EXISTS Status (
  PK_Status INTEGER NOT NULL,
  Type INTEGER NOT NULL,
  Date_Time INTEGER NOT NULL,
  PK_Order INTEGER NOT NULL
    REFERENCES `Order`(PK_Order)
    ON DELETE CASCADE,
  PRIMARY KEY (PK_Status, PK_Order) -- TODO
);

---

CREATE TABLE IF NOT EXISTS Order_Service (
  PK_Order_Service INTEGER NOT NULL,
  PK_Order INTEGER NOT NULL
    REFERENCES `Order`(PK_Order)
    ON DELETE CASCADE,
  PK_Service INTEGER NOT NULL
    REFERENCES Service(PK_Service)
    ON DELETE RESTRICT,
  Date INTEGER NOT NULL,
  PRIMARY KEY (PK_Order_Service, PK_Order, PK_Service) -- TODO
);
