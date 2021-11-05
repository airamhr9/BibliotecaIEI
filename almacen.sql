CREATE TABLE provincia (
	codigo int PRIMARY KEY,
	nombre VARCHAR(20)
);

CREATE TABLE localidad (
	codigo int PRIMARY KEY,
	nombre VARCHAR (30),
	en_provincia int,
	CONSTRAINT fk_provincia 
	  FOREIGN KEY(en_provincia) 
	    REFERENCES provincia(codigo)
);

CREATE TABLE biblioteca (
	nombre VARCHAR(50),
	tipo VARCHAR(10),
	direccion VARCHAR(60),
	codigoPostal int,
	longitud float,
	latitud float,
	telefono int,
	email VARCHAR(40),
	descripcion VARCHAR(100),
	en_localidad int,
	FOREIGN KEY(en_localidad) REFERENCES localidad(codigo)
);
