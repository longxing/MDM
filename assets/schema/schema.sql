CREATE TABLE [WIFI] (
  [id] INTEGER PRIMARY KEY AUTOINCREMENT,
  [time] TEXT,
  [rx] TEXT,
  [tx] TEXT,
  );
  CREATE TABLE [GPRS] (
  [id] INTEGER PRIMARY KEY AUTOINCREMENT,
  [time] TEXT,
  [rx] TEXT,
  [tx] TEXT,
  );
  
CREATE TABLE [CMD] (
  [id] INTEGER PRIMARY KEY AUTOINCREMENT,
  [time] TEXT,
  [name] TEXT,
  [json] TEXT,
  [type] TEXT
  );

