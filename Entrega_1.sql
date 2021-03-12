DROP TABLE CARRO CASCADE CONSTRAINTS;
DROP TABLE MULTA CASCADE CONSTRAINTS;
DROP TABLE CONDUCTOR CASCADE CONSTRAINTS;
--
CREATE TABLE CARRO (
    ID NUMBER(10,0) GENERATED ALWAYS AS IDENTITY,
    PLACA VARCHAR2(50) NOT NULL,
    MARCA VARCHAR2(50) NOT NULL,
    ELECTRICO VARCHAR2(50) NOT NULL,
    CONSTRAINT CARRO_ELECTRICO CHECK(ELECTRICO='SI' OR ELECTRICO='NO'),
    ACTIVO VARCHAR2(2)DEFAULT 'SI',
    CONSTRAINT CHECK_ESTADO CHECK(ACTIVO IS NOT NULL),
    CONSTRAINT CHECK_ACTIVO CHECK(ACTIVO='SI' OR ACTIVO='NO'),
    CONSTRAINT ID_PK_CARRO PRIMARY KEY (ID),
    UNIQUE (PLACA)
);
--
CREATE TABLE CONDUCTOR (
    ID NUMBER(10,0) GENERATED ALWAYS AS IDENTITY,
    CEDULA VARCHAR2(50) NOT NULL,
    NOMBRE VARCHAR2(50) NOT NULL,
    CONSTRAINT ID_PK_CONDUCTOR PRIMARY KEY (ID),
    UNIQUE (CEDULA)
);
--
CREATE TABLE MULTA (
    ID NUMBER(10,0) GENERATED ALWAYS AS IDENTITY,
    IDCARRO NUMBER(10,0) NOT NULL,
    IDCONDUCTOR NUMBER(10,0) NOT NULL,    
    MULTA INT DEFAULT 0,
    FECHA DATE,    
    CONSTRAINT VALID_DATE_MULTA CHECK (FECHA IS NOT NULL),
    CONSTRAINT CHECK_MULTA_NOT_NULL CHECK(MULTA IS NOT NULL),
    CONSTRAINT CHECK_VALOR_NOT_NEGATIVE CHECK (MULTA>=0),
    CONSTRAINT IDCARRO_FK FOREIGN KEY (IDCARRO) REFERENCES CARRO (ID),
    CONSTRAINT IDCONDUCTOR_FK FOREIGN KEY (IDCONDUCTOR) REFERENCES CONDUCTOR (ID),
    CONSTRAINT ID_PK_MULTA PRIMARY KEY (ID)
);
--
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('ABC123', 'HYUNDAI', 'NO', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('ABC124', 'CHEVROLET', 'SI', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('ABC125', 'TOYOTA', 'NO', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('ABC126', 'NISSAN', 'SI', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('ABC127', 'MAZDA', 'NO', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('CBA113', 'HYUNDAI', 'NO', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('CBA114', 'CHEVROLET', 'SI', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('CBA115', 'TOYOTA', 'NO', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('CBA116', 'NISSAN', 'SI', 'SI');
INSERT INTO CARRO(PLACA, MARCA, ELECTRICO, ACTIVO)
VALUES ('CBA117', 'MAZDA', 'NO', 'SI');
--
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000000001', 'JUAN CASAS');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000000010', 'ARMANDO CASAS');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000000011', 'LUCAS CASAS');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000000100', 'PABLO PICASSO');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000000101', 'FEDERICO HERNANDEZ');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000000111', 'JOHN CASABLANCA');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000001000', 'JHON CASAS');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000001001', 'PEDRO RUIZ');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000001011', 'LUCAS FERNANDEZ');
INSERT INTO CONDUCTOR(CEDULA, NOMBRE)
VALUES ('1000001111', 'KEVIN JAMES');
/*
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (5, 1, 100000, TO_DATE('JANUARY 01, 1999', 'MONTH DD, YYYY'));
*/
--
/*
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
    SELECT CARRO.ID, CONDUCTOR.ID, (1000000*(CARRO.ID)*(CONDUCTOR.ID)+9827*CARRO.ID*(CONDUCTOR.ID)), (SELECT TO_DATE (
              TRUNC (
                     DBMS_RANDOM.VALUE (2451545, 2451545+523*(CARRO.ID+CONDUCTOR.ID))
                    )
                , 'J'
              )
  FROM DUAL)
    FROM CARRO, CONDUCTOR;
    *7
    */
--INSERTAR DATOS MULTA
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (3, 5, 100000,TO_DATE('17/01/2020', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (5, 3, 800000,TO_DATE('17/12/2014', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (3, 5, 100000,TO_DATE('17/12/2015', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (2, 6, 200000,TO_DATE('17/12/2015', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (1, 2, 1000,TO_DATE('17/12/2015', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (5, 1, 500,TO_DATE('17/12/2015', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (5, 1, 800000,TO_DATE('17/12/2015', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (5, 1, 150000,TO_DATE('17/11/2015', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (5, 2, 5600000,TO_DATE('17/11/2015', 'DD/MM/YYYY'));
INSERT INTO MULTA(IDCARRO, IDCONDUCTOR, MULTA, FECHA)
VALUES (5, 1, 50000,TO_DATE('17/11/2015', 'DD/MM/YYYY'));

/*¿Cuál es el valor total de multas por año? La vista debe tener el año y el valor total de multas
Utilice la opción EXTRACT(year from fecha) para extraer el año. Asegúrese que en el resultado aparezcan filas de diferentes años*/
CREATE OR REPLACE VIEW MULTAS_ANIO_MES_ELECTRICOS AS (
   SELECT EXTRACT(YEAR FROM MULTA.FECHA) AS ANIO, SUM(VALOR) AS VALOR_TOTAL
   FROM MULTA
   GROUP BY EXTRACT(YEAR FROM MULTA.FECHA
);

--SELECT * FROM MULTAS_ANIO_MES_NOELECTRICOS;

--¿Cuál es el valor total de las multas de un año, mes para los carros eléctricos?La vista debe tener las columnas año, mes, valor total multas
CREATE OR REPLACE VIEW ANIO_MES_VALOR_ELECTRICOS AS(
    SELECT EXTRACT(YEAR FROM MULTA.FECHA) AS ANIO, EXTRACT(MONTH FROM MULTA.FECHA) AS MES, MULTA.MULTA AS VALOR
    FROM MULTA, CARRO 
    WHERE CARRO.ID=MULTA.IDCARRO AND CARRO.ELECTRICO='SI'
);
CREATE OR REPLACE VIEW MULTAS_ANIO_MES_ELECTRICOS AS (
   SELECT ANIO, MES, SUM(VALOR) AS VALOR_TOTAL
   FROM ANIO_MES_VALOR_ELECTRICOS
   GROUP BY ANIO, MES
);

--SELECT * FROM MULTAS_ANIO_MES_ELECTRICOS;
--¿Cuál es el valor total de las multas de un año, mes para los carros no eléctricos?La vista debe tener las columnas año, mes, valor total multas
CREATE OR REPLACE VIEW ANIO_MES_VALOR_NOELECTRICOS AS(
    SELECT EXTRACT(YEAR FROM MULTA.FECHA) AS ANIO, EXTRACT(MONTH FROM MULTA.FECHA) AS MES, MULTA.MULTA AS VALOR
    FROM MULTA, CARRO 
    WHERE CARRO.ID=MULTA.IDCARRO AND CARRO.ELECTRICO='NO'
);
CREATE OR REPLACE VIEW MULTAS_ANIO_MES_NOELECTRICOS AS (
   SELECT ANIO, MES, SUM(VALOR) AS VALOR_TOTAL
   FROM ANIO_MES_VALOR_NOELECTRICOS
   GROUP BY ANIO, MES
);
--SELECT * FROM MULTAS_ANIO;
--
--CONSULTA 1.Produzca un listado que contenga nombre y cedula del conductor, placa del carro, fecha y valor de la multa, ordenado por cedula y placa.
SELECT CO.NOMBRE AS NOMBRE, CO.CEDULA AS CEDULA, CA.PLACA AS PLACA_DEL_CARRO, M.FECHA AS FECHA, M.MULTA AS VALOR_DE_LA_MULTA
FROM CARRO CA, CONDUCTOR CO, MULTA M
WHERE CO.ID=M.IDCONDUCTOR AND CA.ID=M.IDCARRO
ORDER BY CEDULA, PLACA;

----CONSULTA 2
/*2. ¿Cuántas multas tiene cada conductor?
Liste el nombre y cédula del conductor, cantidad total de multas.
Los conductores que no tengan multas deben aparecer en el resultado con 0 en la cantidad.
a. Asegúrese que en el resultado aparezca un conductor que no tenga multas.
*/
SELECT CO.NOMBRE AS NOMBRE,CO.CEDULA AS CEDULA,COUNT(M.MULTA) AS CANTIDAD_MULTAS
FROM CONDUCTOR CO
FULL OUTER JOIN MULTA M
ON CO.ID=M.IDCONDUCTOR 
GROUP BY NOMBRE,CEDULA
ORDER BY CANTIDAD_MULTAS DESC, NOMBRE;

--CONSULTA 3
/*
3. ¿Qué valor total de multas tiene cada carro por año, mes? Liste placa, año, mes y valor total de multas.

*/
CREATE OR REPLACE VIEW MES_ANIO AS(
SELECT C.PLACA AS PLACA,EXTRACT(MONTH FROM M.FECHA) AS MES, EXTRACT(YEAR FROM M.FECHA) AS ANIO,SUM(M.MULTA) AS VALOR_MULTA
FROM MULTA M, CARRO C
WHERE C.ID=M.IDCONDUCTOR
GROUP BY PLACA,EXTRACT(MONTH FROM M.FECHA), EXTRACT(YEAR FROM M.FECHA)
);
SELECT V.PLACA AS PLACA, V.MES AS MES,V.ANIO AS ANIO, V.VALOR_MULTA AS TOTAL
FROM MES_ANIO  V
ORDER BY ANIO,MES,PLACA;

--CONSULTA 7.Actualice la columna ACTIVO de CARRO teniendo en cuenta que se debe asignar ‘NO’ a la columna si:
--a.El carro(s) es el que tiene el mayor número de multas.
CREATE OR REPLACE VIEW MULTAS_POR_CARRO AS(
    SELECT IDCARRO, COUNT(*) AS TOT
    FROM MULTA
    GROUP BY IDCARRO
);

CREATE OR REPLACE VIEW MAXIMUM AS (
  SELECT IDCARRO, TOT
  FROM MULTAS_POR_CARRO
  WHERE TOT IN (SELECT MAX(TOT) FROM MULTAS_POR_CARRO)
);

--SELECT * FROM MULTAS_POR_CARRO;

--SELECT * FROM MAXIMUM;

--SELECT * FROM CARRO;
UPDATE CARRO
SET ACTIVO='NO'
WHERE ID IN (SELECT IDCARRO FROM MAXIMUM);
--SELECT * FROM CARRO;
