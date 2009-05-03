CREATE OR REPLACE PACKAGE TP1_BD.PQ_TIENE_ASIGNADO IS

FUNCTION f_validar_ingreso_update(
                pnIdVendedor            IN  TIENE_ASIGNADO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  TIENE_ASIGNADO.ID_CONTRATO%TYPE,
                pnImporteDeVenta        IN  TIENE_ASIGNADO.IMPORTE_DE_VENTA%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnIdComprador           IN  TIENE_ASIGNADO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  TIENE_ASIGNADO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  TIENE_ASIGNADO.ID_CONTRATO%TYPE,
                pdFechaCompra           IN  TIENE_ASIGNADO.FECHA_COMPRA%TYPE,
                pnComisionVendedor      IN  TIENE_ASIGNADO.COMISION_VENDEDOR%TYPE,
                pnImporteDeVenta        IN  TIENE_ASIGNADO.IMPORTE_DE_VENTA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnIdComprador           IN  TIENE_ASIGNADO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  TIENE_ASIGNADO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  TIENE_ASIGNADO.ID_CONTRATO%TYPE,
                pdFechaCompra           IN  TIENE_ASIGNADO.FECHA_COMPRA%TYPE,
                pnComisionVendedor      IN  TIENE_ASIGNADO.COMISION_VENDEDOR%TYPE,
                pnImporteDeVenta        IN  TIENE_ASIGNADO.IMPORTE_DE_VENTA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnIdComprador           IN  TIENE_ASIGNADO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  TIENE_ASIGNADO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  TIENE_ASIGNADO.ID_CONTRATO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_TIENE_ASIGNADO; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_TIENE_ASIGNADO IS



FUNCTION f_validar_ingreso_update(
                pnIdVendedor            IN  TIENE_ASIGNADO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  TIENE_ASIGNADO.ID_CONTRATO%TYPE,
                pnImporteDeVenta        IN  TIENE_ASIGNADO.IMPORTE_DE_VENTA%TYPE
                ) RETURN BOOLEAN IS

        vRESULTADO      BOOLEAN;
        vCANTIDAD       NUMBER;
    BEGIN
        vRESULTADO := TRUE;
        SELECT COUNT(*) INTO vCANTIDAD
        FROM TIENE_ASIGNADO TA
        WHERE TA.IMPORTE_DE_VENTA IS NOT NULL
            AND TA.ID_VENDEDOR != pnIdVendedor 
            AND TA.ID_CONTRATO = pnIdContrato;
        
        IF (vCANTIDAD > 0) THEN
            vRESULTADO := FALSE;
        END IF;
        
        RETURN vRESULTADO;

  EXCEPTION
    WHEN OTHERS THEN
      RETURN FALSE;
END f_validar_ingreso_update;                

PROCEDURE sp_insert(
                pnIdComprador           IN  TIENE_ASIGNADO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  TIENE_ASIGNADO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  TIENE_ASIGNADO.ID_CONTRATO%TYPE,
                pdFechaCompra           IN  TIENE_ASIGNADO.FECHA_COMPRA%TYPE,
                pnComisionVendedor      IN  TIENE_ASIGNADO.COMISION_VENDEDOR%TYPE,
                pnImporteDeVenta        IN  TIENE_ASIGNADO.IMPORTE_DE_VENTA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        IF (f_validar_ingreso_update(pnIdVendedor, pnIdComprador, pnImporteDeVenta)) THEN
            Insert into TIENE_ASIGNADO
                (ID_COMPRADOR, 
                 ID_VENDEDOR,
                 ID_CONTRATO,
                 FECHA_COMPRA,
                 COMISION_VENDEDOR,
                 IMPORTE_DE_VENTA)
               Values
                (pnIdComprador,
                 pnIdVendedor,
                 pnIdContrato,
                 pdFechaCompra,
                 pnComisionVendedor,
                 pnImporteDeVenta);
            pn_err_code_out := 0;
        ELSE
            pn_err_code_out := -1;
            pv_err_msg_out  := 'No se puede vender dos veces el mismo contrato.';
        END IF;


  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnIdComprador           IN  TIENE_ASIGNADO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  TIENE_ASIGNADO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  TIENE_ASIGNADO.ID_CONTRATO%TYPE,
                pdFechaCompra           IN  TIENE_ASIGNADO.FECHA_COMPRA%TYPE,
                pnComisionVendedor      IN  TIENE_ASIGNADO.COMISION_VENDEDOR%TYPE,
                pnImporteDeVenta        IN  TIENE_ASIGNADO.IMPORTE_DE_VENTA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        IF (f_validar_ingreso_update(pnIdVendedor, pnIdComprador, pnImporteDeVenta)) THEN
            update TIENE_ASIGNADO
                set FECHA_COMPRA        = pdFechaCompra,
                    COMISION_VENDEDOR   = pnComisionVendedor,
                    IMPORTE_DE_VENTA    = pnImporteDeVenta 
                where ID_COMPRADOR   = pnIdComprador
                    and ID_VENDEDOR = pnIdVendedor
                    and ID_CONTRATO = pnIdContrato;
            pn_err_code_out := 0;
        ELSE
            pn_err_code_out := -1;
            pv_err_msg_out  := 'No se puede vender dos veces el mismo contrato.';
        END IF;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnIdComprador           IN  TIENE_ASIGNADO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  TIENE_ASIGNADO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  TIENE_ASIGNADO.ID_CONTRATO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from TIENE_ASIGNADO 
            where ID_COMPRADOR = pnIdComprador
                and ID_VENDEDOR = pnIdVendedor
                and ID_CONTRATO = pnIdContrato;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_TIENE_ASIGNADO; 
/

