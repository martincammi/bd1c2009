CREATE OR REPLACE PACKAGE TP1_BD.PQ_COMPRADOR IS

FUNCTION f_exist_id(
                pnId        IN COMPRADOR.ID_COMPRADOR%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT COMPRADOR.ID_COMPRADOR%TYPE,
                pvApellido              IN  COMPRADOR.APELLIDO%TYPE,
                pvNombre                IN  COMPRADOR.NOMBRE%TYPE,
                pvDomicilio             IN  COMPRADOR.DOMICILIO%TYPE,
                pvTelefono              IN  COMPRADOR.TELEFONO%TYPE,
                pvEmail                 IN  COMPRADOR.EMAIL%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  COMPRADOR.ID_COMPRADOR%TYPE,
                pvApellido              IN  COMPRADOR.APELLIDO%TYPE,
                pvNombre                IN  COMPRADOR.NOMBRE%TYPE,
                pvDomicilio             IN  COMPRADOR.DOMICILIO%TYPE,
                pvTelefono              IN  COMPRADOR.TELEFONO%TYPE,
                pvEmail                 IN  COMPRADOR.EMAIL%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  COMPRADOR.ID_COMPRADOR%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_COMPRADOR; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_COMPRADOR IS

FUNCTION f_exist_id(
                pnId        IN COMPRADOR.ID_COMPRADOR%TYPE
                ) RETURN BOOLEAN IS

        vID      COMPRADOR.ID_COMPRADOR%TYPE;                
    BEGIN
        SELECT ID_COMPRADOR INTO vID FROM COMPRADOR WHERE ID_COMPRADOR = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert(
                pnId                    OUT COMPRADOR.ID_COMPRADOR%TYPE,
                pvApellido              IN  COMPRADOR.APELLIDO%TYPE,
                pvNombre                IN  COMPRADOR.NOMBRE%TYPE,
                pvDomicilio             IN  COMPRADOR.DOMICILIO%TYPE,
                pvTelefono              IN  COMPRADOR.TELEFONO%TYPE,
                pvEmail                 IN  COMPRADOR.EMAIL%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_COMPRADOR.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_COMPRADOR.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into COMPRADOR
            (ID_COMPRADOR, 
             APELLIDO, 
             NOMBRE, 
             DOMICILIO, 
             TELEFONO, 
             EMAIL)
           Values
            (pnId,
             pvApellido,
             pvNombre,
             pvDomicilio,
             pvTelefono,
             pvEmail);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnId                    IN  COMPRADOR.ID_COMPRADOR%TYPE,
                pvApellido              IN  COMPRADOR.APELLIDO%TYPE,
                pvNombre                IN  COMPRADOR.NOMBRE%TYPE,
                pvDomicilio             IN  COMPRADOR.DOMICILIO%TYPE,
                pvTelefono              IN  COMPRADOR.TELEFONO%TYPE,
                pvEmail                 IN  COMPRADOR.EMAIL%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update COMPRADOR
            set APELLIDO    = pvApellido, 
                NOMBRE      = pvNombre, 
                DOMICILIO   = pvDomicilio, 
                TELEFONO    = pvTelefono, 
                EMAIL       = pvEmail
            where ID_COMPRADOR = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  COMPRADOR.ID_COMPRADOR%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from COMPRADOR where ID_COMPRADOR = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_COMPRADOR; 
/

