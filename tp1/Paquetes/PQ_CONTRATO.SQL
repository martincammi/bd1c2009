CREATE OR REPLACE PACKAGE TP1_BD.PQ_CONTRATO IS

FUNCTION f_exist_id(
                pnId        IN CONTRATO.ID_CONTRATO%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert_alquiler(
                pnId                    OUT CONTRATO.ID_CONTRATO%TYPE,
                pnIdInmueble            IN  CONTRATO.ID_INMUEBLE%TYPE,
                pdFechaFirma            IN  CONTRATO.FECHA_FIRMA%TYPE,
                pdVigenciaInicio        IN  CONTRATO.VIGENCIA_INICIO%TYPE,
                pdVigenciaFin           IN  CONTRATO.VIGENCIA_FIN%TYPE,
                pnImporteAcordado       IN  CONTRATO_ALQUILER.IMPORTE_ACORDADO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_insert_venta(
                pnId                    OUT CONTRATO.ID_CONTRATO%TYPE,
                pnIdInmueble            IN  CONTRATO.ID_INMUEBLE%TYPE,
                pdFechaFirma            IN  CONTRATO.FECHA_FIRMA%TYPE,
                pdVigenciaInicio        IN  CONTRATO.VIGENCIA_INICIO%TYPE,
                pdVigenciaFin           IN  CONTRATO.VIGENCIA_FIN%TYPE,
                pnComisionInmobiliaria  IN  CONTRATO_VENTA.COMISION_INMOBILIARIA%TYPE,
                pnPrecioInicialVenta    IN  CONTRATO_VENTA.PRECIO_INICIAL_VENTA%TYPE,
                pnPrecioMinimo          IN  CONTRATO_VENTA.PRECIO_MINIMO%TYPE,
                pvCondicionesVisita     IN  CONTRATO_VENTA.CONDICIONES_VISITA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update_alquiler(
                pnId                    IN  CONTRATO.ID_CONTRATO%TYPE,
                pnIdInmueble            IN  CONTRATO.ID_INMUEBLE%TYPE,
                pdFechaFirma            IN  CONTRATO.FECHA_FIRMA%TYPE,
                pdVigenciaInicio        IN  CONTRATO.VIGENCIA_INICIO%TYPE,
                pdVigenciaFin           IN  CONTRATO.VIGENCIA_FIN%TYPE,
                pnImporteAcordado       IN  CONTRATO_ALQUILER.IMPORTE_ACORDADO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update_venta(
                pnId                    OUT CONTRATO.ID_CONTRATO%TYPE,
                pnIdInmueble            IN  CONTRATO.ID_INMUEBLE%TYPE,
                pdFechaFirma            IN  CONTRATO.FECHA_FIRMA%TYPE,
                pdVigenciaInicio        IN  CONTRATO.VIGENCIA_INICIO%TYPE,
                pdVigenciaFin           IN  CONTRATO.VIGENCIA_FIN%TYPE,
                pnComisionInmobiliaria  IN  CONTRATO_VENTA.COMISION_INMOBILIARIA%TYPE,
                pnPrecioInicialVenta    IN  CONTRATO_VENTA.PRECIO_INICIAL_VENTA%TYPE,
                pnPrecioMinimo          IN  CONTRATO_VENTA.PRECIO_MINIMO%TYPE,
                pvCondicionesVisita     IN  CONTRATO_VENTA.CONDICIONES_VISITA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  CONTRATO.ID_CONTRATO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_CONTRATO; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_CONTRATO IS

FUNCTION f_exist_id(
                pnId        IN CONTRATO.ID_CONTRATO%TYPE
                ) RETURN BOOLEAN IS

        vID      CONTRATO.ID_CONTRATO%TYPE;                
    BEGIN
        SELECT ID_CONTRATO INTO vID FROM CONTRATO WHERE ID_CONTRATO = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert_alquiler(
                pnId                    OUT CONTRATO.ID_CONTRATO%TYPE,
                pnIdInmueble            IN  CONTRATO.ID_INMUEBLE%TYPE,
                pdFechaFirma            IN  CONTRATO.FECHA_FIRMA%TYPE,
                pdVigenciaInicio        IN  CONTRATO.VIGENCIA_INICIO%TYPE,
                pdVigenciaFin           IN  CONTRATO.VIGENCIA_FIN%TYPE,
                pnImporteAcordado       IN  CONTRATO_ALQUILER.IMPORTE_ACORDADO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_CONTRATO.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_CONTRATO.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into CONTRATO
            (ID_CONTRATO, 
             ID_INMUEBLE,
             FECHA_FIRMA,
             VIGENCIA_INICIO,
             VIGENCIA_FIN,
             TIPO_CONTRATO)
           Values
            (pnId,
             pnIdInmueble,
             pdFechaFirma,
             pdVigenciaInicio,
             pdVigenciaFin,
             'Alquiler');

        Insert into CONTRATO_ALQUILER
            (ID_CONTRATO, 
             IMPORTE_ACORDADO)
           Values
            (pnId,
             pnImporteAcordado);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert_alquiler;                

PROCEDURE sp_insert_venta(
                pnId                    OUT CONTRATO.ID_CONTRATO%TYPE,
                pnIdInmueble            IN  CONTRATO.ID_INMUEBLE%TYPE,
                pdFechaFirma            IN  CONTRATO.FECHA_FIRMA%TYPE,
                pdVigenciaInicio        IN  CONTRATO.VIGENCIA_INICIO%TYPE,
                pdVigenciaFin           IN  CONTRATO.VIGENCIA_FIN%TYPE,
                pnComisionInmobiliaria  IN  CONTRATO_VENTA.COMISION_INMOBILIARIA%TYPE,
                pnPrecioInicialVenta    IN  CONTRATO_VENTA.PRECIO_INICIAL_VENTA%TYPE,
                pnPrecioMinimo          IN  CONTRATO_VENTA.PRECIO_MINIMO%TYPE,
                pvCondicionesVisita     IN  CONTRATO_VENTA.CONDICIONES_VISITA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_CONTRATO.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_CONTRATO.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into CONTRATO
            (ID_CONTRATO, 
             ID_INMUEBLE,
             FECHA_FIRMA,
             VIGENCIA_INICIO,
             VIGENCIA_FIN,
             TIPO_CONTRATO)
           Values
            (pnId,
             pnIdInmueble,
             pdFechaFirma,
             pdVigenciaInicio,
             pdVigenciaFin,
             'Venta');

        Insert into CONTRATO_VENTA
            (ID_CONTRATO, 
             COMISION_INMOBILIARIA,
             PRECIO_INICIAL_VENTA,
             PRECIO_MINIMO,
             CONDICIONES_VISITA)
           Values
            (pnId,
             pnComisionInmobiliaria,
             pnPrecioInicialVenta,
             pnPrecioMinimo,
             pvCondicionesVisita);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert_venta;                

PROCEDURE sp_update_alquiler(
                pnId                    IN  CONTRATO.ID_CONTRATO%TYPE,
                pnIdInmueble            IN  CONTRATO.ID_INMUEBLE%TYPE,
                pdFechaFirma            IN  CONTRATO.FECHA_FIRMA%TYPE,
                pdVigenciaInicio        IN  CONTRATO.VIGENCIA_INICIO%TYPE,
                pdVigenciaFin           IN  CONTRATO.VIGENCIA_FIN%TYPE,
                pnImporteAcordado       IN  CONTRATO_ALQUILER.IMPORTE_ACORDADO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update CONTRATO
            set ID_INMUEBLE     = pnIdInmueble,
                FECHA_FIRMA     = pdFechaFirma,
                VIGENCIA_INICIO = pdVigenciaInicio,
                VIGENCIA_FIN    = pdVigenciaFin 
            where ID_CONTRATO = pnId;

        update CONTRATO_ALQUILER
            set IMPORTE_ACORDADO    = pnImporteAcordado 
            where ID_CONTRATO = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update_alquiler;                

PROCEDURE sp_update_venta(
                pnId                    OUT CONTRATO.ID_CONTRATO%TYPE,
                pnIdInmueble            IN  CONTRATO.ID_INMUEBLE%TYPE,
                pdFechaFirma            IN  CONTRATO.FECHA_FIRMA%TYPE,
                pdVigenciaInicio        IN  CONTRATO.VIGENCIA_INICIO%TYPE,
                pdVigenciaFin           IN  CONTRATO.VIGENCIA_FIN%TYPE,
                pnComisionInmobiliaria  IN  CONTRATO_VENTA.COMISION_INMOBILIARIA%TYPE,
                pnPrecioInicialVenta    IN  CONTRATO_VENTA.PRECIO_INICIAL_VENTA%TYPE,
                pnPrecioMinimo          IN  CONTRATO_VENTA.PRECIO_MINIMO%TYPE,
                pvCondicionesVisita     IN  CONTRATO_VENTA.CONDICIONES_VISITA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update CONTRATO
            set ID_INMUEBLE     = pnIdInmueble,
                FECHA_FIRMA     = pdFechaFirma,
                VIGENCIA_INICIO = pdVigenciaInicio,
                VIGENCIA_FIN    = pdVigenciaFin 
            where ID_CONTRATO = pnId;

        update CONTRATO_VENTA
            set COMISION_INMOBILIARIA   = pnComisionInmobiliaria,
                PRECIO_INICIAL_VENTA    = pnPrecioInicialVenta,
                PRECIO_MINIMO           = pnPrecioMinimo,
                CONDICIONES_VISITA      = pvCondicionesVisita 
            where ID_CONTRATO = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update_venta;                

PROCEDURE sp_delete(
                pnId                    IN  CONTRATO.ID_CONTRATO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN
        
        delete from CONTRATO_ALQUILER where ID_CONTRATO = pnId;
        delete from CONTRATO_VENTA where ID_CONTRATO = pnId;
        delete from CONTRATO where ID_CONTRATO = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_CONTRATO; 
/

