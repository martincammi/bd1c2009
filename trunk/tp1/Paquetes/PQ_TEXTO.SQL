CREATE OR REPLACE PACKAGE TP1_BD.PQ_TEXTO IS

FUNCTION f_exist_id(
                pnId        IN TEXTO.ID_TEXTO%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT TEXTO.ID_TEXTO%TYPE,
                pvDescripcion           IN  TEXTO.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  TEXTO.ID_TEXTO%TYPE,
                pvDescripcion           IN  TEXTO.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  TEXTO.ID_TEXTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_TEXTO; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_TEXTO IS

FUNCTION f_exist_id(
                pnId        IN TEXTO.ID_TEXTO%TYPE
                ) RETURN BOOLEAN IS

        vID      TEXTO.ID_TEXTO%TYPE;                
    BEGIN
        SELECT ID_TEXTO INTO vID FROM TEXTO WHERE ID_TEXTO = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert(
                pnId                    OUT TEXTO.ID_TEXTO%TYPE,
                pvDescripcion           IN  TEXTO.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_TEXTO.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_TEXTO.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into TEXTO
            (ID_TEXTO, 
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
                pnId                    IN  TEXTO.ID_TEXTO%TYPE,
                pvDescripcion           IN  TEXTO.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update TEXTO
            set DESCRIPCION    = pvDescripcion 
            where ID_TEXTO = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  TEXTO.ID_TEXTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from TEXTO where ID_TEXTO = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_TEXTO; 
/

