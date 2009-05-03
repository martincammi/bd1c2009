CREATE OR REPLACE PACKAGE TP1_BD.PQ_FOTO_INMUEBLE IS

FUNCTION f_exist_id(
                pnId        IN FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE,
                pnIdInmueble            IN  FOTO_INMUEBLE.ID_INMUEBLE%TYPE,
                pbFotoReal              IN  FOTO_INMUEBLE.FOTO_REAL%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE,
                pnIdInmueble            IN  FOTO_INMUEBLE.ID_INMUEBLE%TYPE,
                pbFotoReal              IN  FOTO_INMUEBLE.FOTO_REAL%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_FOTO_INMUEBLE; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_FOTO_INMUEBLE IS

FUNCTION f_exist_id(
                pnId        IN FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE
                ) RETURN BOOLEAN IS

        vID      FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE;                
    BEGIN
        SELECT ID_FOTO_INMUEBLE INTO vID FROM FOTO_INMUEBLE WHERE ID_FOTO_INMUEBLE = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert(
                pnId                    OUT FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE,
                pnIdInmueble            IN  FOTO_INMUEBLE.ID_INMUEBLE%TYPE,
                pbFotoReal              IN  FOTO_INMUEBLE.FOTO_REAL%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_FOTO_INMUEBLE.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_FOTO_INMUEBLE.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into FOTO_INMUEBLE
            (ID_FOTO_INMUEBLE, 
             ID_INMUEBLE,
             FOTO_REAL)
           Values
            (pnId,
             pnIdInmueble,
             pbFotoReal);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnId                    IN  FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE,
                pnIdInmueble            IN  FOTO_INMUEBLE.ID_INMUEBLE%TYPE,
                pbFotoReal              IN  FOTO_INMUEBLE.FOTO_REAL%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update FOTO_INMUEBLE
            set ID_INMUEBLE     = pnIdInmueble,
                FOTO_REAL       = pbFotoReal 
            where ID_FOTO_INMUEBLE = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  FOTO_INMUEBLE.ID_FOTO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from FOTO_INMUEBLE where ID_FOTO_INMUEBLE = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_FOTO_INMUEBLE; 
/

