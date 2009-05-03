CREATE OR REPLACE PACKAGE TP1_BD.PQ_TIPO_INMUEBLE IS

FUNCTION f_exist_id(
                pnId        IN TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pvDescripcion           IN  TIPO_INMUEBLE.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pvDescripcion           IN  TIPO_INMUEBLE.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_TIPO_INMUEBLE; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_TIPO_INMUEBLE IS

FUNCTION f_exist_id(
                pnId        IN TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE
                ) RETURN BOOLEAN IS

        vID      TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE;                
    BEGIN
        SELECT ID_TIPO_INMUEBLE INTO vID FROM TIPO_INMUEBLE WHERE ID_TIPO_INMUEBLE = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                


PROCEDURE sp_insert(
                pnId                    OUT TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pvDescripcion           IN  TIPO_INMUEBLE.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_TIPO_INMUEBLE.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_TIPO_INMUEBLE.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into TIPO_INMUEBLE
            (ID_TIPO_INMUEBLE, 
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
                pnId                    IN  TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pvDescripcion           IN  TIPO_INMUEBLE.DESCRIPCION%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update TIPO_INMUEBLE
            set DESCRIPCION    = pvDescripcion 
            where ID_TIPO_INMUEBLE = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  TIPO_INMUEBLE.ID_TIPO_INMUEBLE%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from TIPO_INMUEBLE where ID_TIPO_INMUEBLE = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_TIPO_INMUEBLE; 
/

