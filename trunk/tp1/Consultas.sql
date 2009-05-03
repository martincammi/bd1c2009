--Consulta 1 -- Vendedor mejor pago
SELECT V.ID_VENDEDOR, V.APELLIDO, V.DOMICILIO, V.EMAIL, V.EXPERIENCIA, V.TELEFONO, V.FECHA_COMIENZO, SUM(TA.COMISION_VENDEDOR) COMISION, COUNT(*) CANTIDAD_VENTAS
FROM TIENE_ASIGNADO TA
    INNER JOIN VENDEDOR V ON V.ID_VENDEDOR = TA.ID_VENDEDOR
WHERE FECHA_COMPRA BETWEEN TO_DATE('01072008', 'DDMMYYYY') AND TO_DATE('31122008', 'DDMMYYYY')
    AND TA.IMPORTE_DE_VENTA IS NOT NULL
GROUP BY V.ID_VENDEDOR, V.APELLIDO, V.DOMICILIO, V.EMAIL, V.EXPERIENCIA, V.TELEFONO, V.FECHA_COMIENZO
HAVING SUM(TA.COMISION_VENDEDOR) >= ALL (SELECT SUM(TA_2.COMISION_VENDEDOR) COMISION
                                        FROM TIENE_ASIGNADO TA_2
                                        WHERE FECHA_COMPRA BETWEEN TO_DATE('01072008', 'DDMMYYYY') AND TO_DATE('31122008', 'DDMMYYYY')
                                        GROUP BY ID_VENDEDOR)    
                                        
--Consulta 2 -- Vendedores que participaron en todas las ventas
SELECT ID_VENDEDOR, V.APELLIDO, V.DOMICILIO, V.EMAIL
FROM VENDEDOR V
WHERE NOT EXISTS (SELECT ID_VENDEDOR
                  FROM TIENE_ASIGNADO TA
                  WHERE TO_CHAR(TA.FECHA_COMPRA, 'MMYYYY') = '01' || :A
                        AND TA.ID_VENDEDOR != V.ID_VENDEDOR 
                        AND NOT EXISTS (SELECT ID_VENDEDOR
                                    FROM SE_LO_MOSTRO SM
                                    WHERE SM.ID_CONTRATO = TA.ID_CONTRATO
                                        AND SM.ID_VENDEDOR = V.ID_VENDEDOR))

--Consulta 2 -- Vendedores que participaron en todas las ventas, para los que no vendieron el inmueble 
-- solo aquellos que se lo mostraron al comprador que lo compro.
SELECT ID_VENDEDOR, V.APELLIDO, V.DOMICILIO, V.EMAIL
FROM VENDEDOR V
WHERE NOT EXISTS (SELECT ID_VENDEDOR
                  FROM TIENE_ASIGNADO TA
                  WHERE TO_CHAR(TA.FECHA_COMPRA, 'MMYYYY') = '01' || :A
                        AND TA.ID_VENDEDOR != V.ID_VENDEDOR 
                        AND NOT EXISTS (SELECT ID_VENDEDOR
                                    FROM SE_LO_MOSTRO SM
                                    WHERE SM.ID_CONTRATO = TA.ID_CONTRATO
                                        AND SM.ID_VENDEDOR = V.ID_VENDEDOR
                                        AND SM.ID_COMPRADOR = TA.ID_COMPRADOR))



                                             