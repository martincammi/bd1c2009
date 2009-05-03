CREATE OR REPLACE PACKAGE TP1_BD.PQ_INMUEBLE IS

FUNCTION f_exist_id(
                pnId        IN INMUEBLE.ID_INMUEBLE%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT INMUEBLE.ID_INMUEBLE%TYPE,
                pnIdPropietario         IN  INMUEBLE.ID_PROPIETARIO%TYPE,
                pvDireccion             IN  INMUEBLE.DIRECCION%TYPE,
                pvEstado                IN  INMUEBLE.ESTADO%TYPE,
                pnAntiguedad            IN  INMUEBLE.ANTIGUEDAD%TYPE,
                pvPlano                 IN  INMUEBLE.PLANO%TYPE,
                pnMetrosCuadrados       IN  INMUEBLE.METROS_CUADRADOS%TYPE,
                pvOrientacion           IN  INMUEBLE.ORIENTACION%TYPE,
                pnCantidadAmbientes     IN  INMUEBLE.CANTIDAD_AMBIENTES%TYPE,
                pdFechaFinalizacion     IN  INMUEBLE.FECHA_FINALIZACION%TYPE,
                pnIdZona                IN  INMUEBLE.ID_ZONA%TYPE,
                pnIdTipoInmueble        IN  INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  INMUEBLE.ID_INMUEBLE%TYPE,
                pnIdPropietario         IN  INMUEBLE.ID_PROPIETARIO%TYPE,
                pvDireccion             IN  INMUEBLE.DIRECCION%TYPE,
                pvEstado                IN  INMUEBLE.ESTADO%TYPE,
                pnAntiguedad            IN  INMUEBLE.ANTIGUEDAD%TYPE,
                pvPlano                 IN  INMUEBLE.PLANO%TYPE,
                pnMetrosCuadrados       IN  INMUEBLE.METROS_CUADRADOS%TYPE,
                pvOrientacion           IN  INMUEBLE.ORIENTACION%TYPE,
                pnCantidadAmbientes     IN  INMUEBLE.CANTIDAD_AMBIENTES%TYPE,
                pdFechaFinalizacion     IN  INMUEBLE.FECHA_FINALIZACION%TYPE,
                pnIdZona                IN  INMUEBLE.ID_ZONA%TYPE,
                pnIdTipoInmueble        IN  INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  INMUEBLE.ID_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_INMUEBLE; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_INMUEBLE IS

FUNCTION f_exist_id(
                pnId        IN INMUEBLE.ID_INMUEBLE%TYPE
                ) RETURN BOOLEAN IS

        vID      INMUEBLE.ID_INMUEBLE%TYPE;                
    BEGIN
        SELECT ID_INMUEBLE INTO vID FROM INMUEBLE WHERE ID_INMUEBLE = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert(
                pnId                    OUT INMUEBLE.ID_INMUEBLE%TYPE,
                pnIdPropietario         IN  INMUEBLE.ID_PROPIETARIO%TYPE,
                pvDireccion             IN  INMUEBLE.DIRECCION%TYPE,
                pvEstado                IN  INMUEBLE.ESTADO%TYPE,
                pnAntiguedad            IN  INMUEBLE.ANTIGUEDAD%TYPE,
                pvPlano                 IN  INMUEBLE.PLANO%TYPE,
                pnMetrosCuadrados       IN  INMUEBLE.METROS_CUADRADOS%TYPE,
                pvOrientacion           IN  INMUEBLE.ORIENTACION%TYPE,
                pnCantidadAmbientes     IN  INMUEBLE.CANTIDAD_AMBIENTES%TYPE,
                pdFechaFinalizacion     IN  INMUEBLE.FECHA_FINALIZACION%TYPE,
                pnIdZona                IN  INMUEBLE.ID_ZONA%TYPE,
                pnIdTipoInmueble        IN  INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_INMUEBLE.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_INMUEBLE.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into INMUEBLE
            (ID_INMUEBLE, 
             ID_PROPIETARIO,
             DIRECCION,
             ESTADO,
             ANTIGUEDAD,
             PLANO,
             METROS_CUADRADOS,
             ORIENTACION,
             CANTIDAD_AMBIENTES,
             FECHA_FINALIZACION,
             ID_ZONA,
             ID_TIPO_INMUEBLE)
           Values
            (pnId,
             pnIdPropietario,
             pvDireccion,
             pvEstado,
             pnAntiguedad,
             pvPlano,
             pnMetrosCuadrados,
             pvOrientacion,
             pnCantidadAmbientes,
             pdFechaFinalizacion,
             pnIdZona,
             pnIdTipoInmueble);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnId                    IN  INMUEBLE.ID_INMUEBLE%TYPE,
                pnIdPropietario         IN  INMUEBLE.ID_PROPIETARIO%TYPE,
                pvDireccion             IN  INMUEBLE.DIRECCION%TYPE,
                pvEstado                IN  INMUEBLE.ESTADO%TYPE,
                pnAntiguedad            IN  INMUEBLE.ANTIGUEDAD%TYPE,
                pvPlano                 IN  INMUEBLE.PLANO%TYPE,
                pnMetrosCuadrados       IN  INMUEBLE.METROS_CUADRADOS%TYPE,
                pvOrientacion           IN  INMUEBLE.ORIENTACION%TYPE,
                pnCantidadAmbientes     IN  INMUEBLE.CANTIDAD_AMBIENTES%TYPE,
                pdFechaFinalizacion     IN  INMUEBLE.FECHA_FINALIZACION%TYPE,
                pnIdZona                IN  INMUEBLE.ID_ZONA%TYPE,
                pnIdTipoInmueble        IN  INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update INMUEBLE
            set ID_PROPIETARIO          = pnIdPropietario,
                DIRECCION               = pvDireccion,
                ESTADO                  = pvEstado,
                ANTIGUEDAD              = pnAntiguedad,
                PLANO                   = pvPlano,
                METROS_CUADRADOS        = pnMetrosCuadrados,
                ORIENTACION             = pvOrientacion,
                CANTIDAD_AMBIENTES      = pnCantidadAmbientes,
                FECHA_FINALIZACION      = pdFechaFinalizacion,
                ID_ZONA                 = pnIdZona,
                ID_TIPO_INMUEBLE        = pnIdTipoInmueble 
            where ID_INMUEBLE = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  INMUEBLE.ID_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from INMUEBLE where ID_INMUEBLE = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_INMUEBLE; 
/

