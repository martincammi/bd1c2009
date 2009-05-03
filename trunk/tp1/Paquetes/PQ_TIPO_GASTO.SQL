CREATE OR REPLACE PACKAGE TP1_BD.PQ_TIPO_GASTO IS

FUNCTION f_exist_id(
                pnId        IN TIPO_GASTO.ID_TIPO_GASTO%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT TIPO_GASTO.ID_TIPO_GASTO%TYPE,
                pvDescripcion           IN  TIPO_GASTO.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  TIPO_GASTO.ID_TIPO_GASTO%TYPE,
                pvDescripcion           IN  TIPO_GASTO.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  TIPO_GASTO.ID_TIPO_GASTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_TIPO_GASTO; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_TIPO_GASTO IS

FUNCTION f_exist_id(
                pnId        IN TIPO_GASTO.ID_TIPO_GASTO%TYPE
                ) RETURN BOOLEAN IS

        vID      TIPO_GASTO.ID_TIPO_GASTO%TYPE;                
    BEGIN
        SELECT ID_TIPO_GASTO INTO vID FROM TIPO_GASTO WHERE ID_TIPO_GASTO = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert(
                pnId                    OUT TIPO_GASTO.ID_TIPO_GASTO%TYPE,
                pvDescripcion           IN  TIPO_GASTO.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_TIPO_GASTO.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_TIPO_GASTO.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into TIPO_GASTO
            (ID_TIPO_GASTO, 
             DESCRIPCION)
           Values
            (pnId,
             pvDescripcion);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnId                    IN  TIPO_GASTO.ID_TIPO_GASTO%TYPE,
                pvDescripcion           IN  TIPO_GASTO.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update TIPO_GASTO
            set DESCRIPCION    = pvDescripcion 
            where ID_TIPO_GASTO = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  TIPO_GASTO.ID_TIPO_GASTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from TIPO_GASTO where ID_TIPO_GASTO = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_TIPO_GASTO; 
/

