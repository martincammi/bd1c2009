CREATE OR REPLACE PACKAGE TP1_BD.PQ_GASTO_INMUEBLE IS

FUNCTION f_exist_id(
                pnId        IN GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE,
                pnIdInmueble            IN  GASTO_INMUEBLE.ID_INMUEBLE%TYPE,
                pnValor                 IN  GASTO_INMUEBLE.VALOR%TYPE,
                pnIdTipoGasto           IN  GASTO_INMUEBLE.ID_TIPO_GASTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE,
                pnIdInmueble            IN  GASTO_INMUEBLE.ID_INMUEBLE%TYPE,
                pnValor                 IN  GASTO_INMUEBLE.VALOR%TYPE,
                pnIdTipoGasto           IN  GASTO_INMUEBLE.ID_TIPO_GASTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_GASTO_INMUEBLE; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_GASTO_INMUEBLE IS

FUNCTION f_exist_id(
                pnId        IN GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE
                ) RETURN BOOLEAN IS

        vID      GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE;                
    BEGIN
        SELECT ID_GASTO_INMUEBLE INTO vID FROM GASTO_INMUEBLE WHERE ID_GASTO_INMUEBLE = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert(
                pnId                    OUT GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE,
                pnIdInmueble            IN  GASTO_INMUEBLE.ID_INMUEBLE%TYPE,
                pnValor                 IN  GASTO_INMUEBLE.VALOR%TYPE,
                pnIdTipoGasto           IN  GASTO_INMUEBLE.ID_TIPO_GASTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_GASTO_INMUEBLE.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_GASTO_INMUEBLE.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into GASTO_INMUEBLE
            (ID_GASTO_INMUEBLE, 
             ID_INMUEBLE,
             VALOR,
             ID_TIPO_GASTO)
           Values
            (pnId,
             pnIdInmueble,
             pnValor,
             pnIdTipoGasto);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnId                    IN  GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE,
                pnIdInmueble            IN  GASTO_INMUEBLE.ID_INMUEBLE%TYPE,
                pnValor                 IN  GASTO_INMUEBLE.VALOR%TYPE,
                pnIdTipoGasto           IN  GASTO_INMUEBLE.ID_TIPO_GASTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update GASTO_INMUEBLE
            set ID_INMUEBLE     = pnIdInmueble,
                VALOR           = pnValor,
                ID_TIPO_GASTO   = pnIdTipoGasto
            where ID_GASTO_INMUEBLE = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  GASTO_INMUEBLE.ID_GASTO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from GASTO_INMUEBLE where ID_GASTO_INMUEBLE = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_GASTO_INMUEBLE; 
/

