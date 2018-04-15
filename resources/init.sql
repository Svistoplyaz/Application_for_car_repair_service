CREATE TABLE IF NOT EXISTS Users (
  PK_User INTEGER PRIMARY KEY NOT NULL,
  Password Text NOT NULL
);

---

INSERT OR IGNORE INTO Users (PK_User, Password) VALUES (0, '123');

---

INSERT OR IGNORE INTO Users (PK_User, Password) VALUES (1, 'qwe');

---

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

CREATE TABLE IF NOT EXISTS Position (
  PK_Position INTEGER PRIMARY KEY NOT NULL,
  Name TEXT(30) NOT NULL
);

---

CREATE TABLE IF NOT EXISTS Staff (
  PK_Staff INTEGER PRIMARY KEY NOT NULL,
  PK_Position INTEGER NOT NULL
    REFERENCES Position(PK_Position)
    ON DELETE RESTRICT,
  Name TEXT(100) NOT NULL,
  Phone TEXT(20) NOT NULL,
  Birth_day INTEGER NOT NULL
);

---

CREATE TABLE IF NOT EXISTS `Order` (
  PK_Order INTEGER PRIMARY KEY NOT NULL,
  PK_Clients INTEGER NOT NULL
    REFERENCES Clients(PK_Clients)
    ON DELETE RESTRICT,
  PK_Staff INTEGER NOT NULL
    REFERENCES Staff(PK_Staff)
    ON DELETE RESTRICT,
  Registration_number TEXT(20),
  Start_date INTEGER NOT NULL,
  Finish_date INTEGER,
  PK_Model INTEGER NOT NULL
    REFERENCES Model(PK_Model)
      ON DELETE RESTRICT,
  Finish_cost INTEGER
);

---

CREATE TABLE IF NOT EXISTS Status (
  PK_Status INTEGER PRIMARY KEY NOT NULL,
  Type INTEGER NOT NULL,
  Date_Time INTEGER NOT NULL,
  PK_Order INTEGER NOT NULL
    REFERENCES `Order`(PK_Order)
    ON DELETE CASCADE
);

---

CREATE TABLE IF NOT EXISTS Order_Service (
  PK_Order_Service INTEGER PRIMARY KEY NOT NULL,
  PK_Order INTEGER NOT NULL
    REFERENCES `Order`(PK_Order)
    ON DELETE CASCADE,
  PK_Service INTEGER NOT NULL
    REFERENCES Service(PK_Service)
    ON DELETE RESTRICT,
  Date INTEGER NOT NULL
);

---

CREATE TABLE IF NOT EXISTS Spare_part (
  PK_Spare_part INTEGER PRIMARY KEY NOT NULL,
  Name TEXT(100) NOT NULL,
  Quantity INTEGER NOT NULL,
  Unit INTEGER NOT NULL,
  Universal INTEGER NOT NULL
);

---

CREATE TABLE IF NOT EXISTS Spare_part_price (
  PK_Spare_part_price INTEGER PRIMARY KEY NOT NULL,
  Date INTEGER NOT NULL,
  Price INTEGER NOT NULL,
  PK_SparePart INTEGER NOT NULL
    REFERENCES Spare_part(PK_Spare_part)
    ON DELETE CASCADE
);

---

CREATE TABLE IF NOT EXISTS Model_Spare_part (
  PK_Model_Spare_part INTEGER PRIMARY KEY NOT NULL,
  PK_Model INTEGER NOT NULL
    REFERENCES Model(PK_Model)
    ON DELETE CASCADE,
  PK_Spare_part INTEGER NOT NULL
    REFERENCES Spare_part(PK_Spare_part)
    ON DELETE CASCADE
);

---

CREATE TABLE IF NOT EXISTS Spare_part_Order (
  PK_Spare_part_Order INTEGER PRIMARY KEY NOT NULL,
  PK_Spare_part INTEGER NOT NULL
    REFERENCES Spare_part(PK_Spare_part)
    ON DELETE RESTRICT,
  PK_Order INTEGER NOT NULL
    REFERENCES `Order`(PK_Order)
    ON DELETE CASCADE,
  Date INTEGER NOT NULL,
  Quantity INTEGER NOT NULL
);

---

CREATE VIEW IF NOT EXISTS
  Reservation (Quantity, PartName, StaffName, PK_Order, Date, Status)
AS SELECT
  po.Quantity, po.PK_Spare_part, o.PK_Staff, o.PK_Order, po.Date,
  (SELECT st.Type FROM Status st WHERE st.PK_Order = o.PK_Order ORDER BY st.PK_Status DESC LIMIT 1) as Status
FROM
  Spare_part_Order po, `Order` o
WHERE
  po.PK_Order = o.PK_Order
  AND Status <> 2
ORDER BY
  po.Date DESC;

---

CREATE TABLE IF NOT EXISTS Purchase (
  PK_Purchase INTEGER PRIMARY KEY NOT NULL,
  PK_Spare_part INTEGER NOT NULL
    REFERENCES Spare_part(PK_Spare_part)
    ON DELETE RESTRICT,
  Quantity INTEGER NOT NULL,
  Price INTEGER NOT NULL,
  Date INTEGER NOT NULL
);
