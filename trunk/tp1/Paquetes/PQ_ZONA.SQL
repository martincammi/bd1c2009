CREATE OR REPLACE PACKAGE TP1_BD.PQ_ZONA IS

FUNCTION f_exist_id(
                pnId        IN ZONA.ID_ZONA%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT ZONA.ID_ZONA%TYPE,
                pvDescripcion           IN  ZONA.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  ZONA.ID_ZONA%TYPE,
                pvDescripcion           IN  ZONA.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  ZONA.ID_ZONA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_ZONA; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_ZONA IS

FUNCTION f_exist_id(
                pnId        IN ZONA.ID_ZONA%TYPE
                ) RETURN BOOLEAN IS

        vID      ZONA.ID_ZONA%TYPE;                
    BEGIN
        SELECT ID_ZONA INTO vID FROM ZONA WHERE ID_ZONA = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert(
                pnId                    OUT ZONA.ID_ZONA%TYPE,
                pvDescripcion           IN  ZONA.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_ZONA.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_ZONA.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into ZONA
            (ID_ZONA, 
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
                pnId                    IN  ZONA.ID_ZONA%TYPE,
                pvDescripcion           IN  ZONA.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update ZONA
            set DESCRIPCION    = pvDescripcion 
            where ID_ZONA = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  ZONA.ID_ZONA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from ZONA where ID_ZONA = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_ZONA; 
/

