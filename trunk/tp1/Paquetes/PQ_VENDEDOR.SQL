CREATE OR REPLACE PACKAGE TP1_BD.PQ_VENDEDOR IS

FUNCTION f_exist_id(
                pnId        IN VENDEDOR.ID_VENDEDOR%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert(
                pnId                    OUT VENDEDOR.ID_VENDEDOR%TYPE,
                pvApellido              IN  VENDEDOR.APELLIDO%TYPE,
                pvDomicilio             IN  VENDEDOR.DOMICILIO%TYPE,
                pvTelefono              IN  VENDEDOR.TELEFONO%TYPE,
                pvEmail                 IN  VENDEDOR.EMAIL%TYPE,
                pdFechaComienzo         IN  VENDEDOR.FECHA_COMIENZO%TYPE,
                pvExperiencia           IN  VENDEDOR.EXPERIENCIA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnId                    IN  VENDEDOR.ID_VENDEDOR%TYPE,
                pvApellido              IN  VENDEDOR.APELLIDO%TYPE,
                pvDomicilio             IN  VENDEDOR.DOMICILIO%TYPE,
                pvTelefono              IN  VENDEDOR.TELEFONO%TYPE,
                pvEmail                 IN  VENDEDOR.EMAIL%TYPE,
                pdFechaComienzo         IN  VENDEDOR.FECHA_COMIENZO%TYPE,
                pvExperiencia           IN  VENDEDOR.EXPERIENCIA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  VENDEDOR.ID_VENDEDOR%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_VENDEDOR; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_VENDEDOR IS

FUNCTION f_exist_id(
                pnId        IN VENDEDOR.ID_VENDEDOR%TYPE
                ) RETURN BOOLEAN IS

        vID      VENDEDOR.ID_VENDEDOR%TYPE;                
    BEGIN
        SELECT ID_VENDEDOR INTO vID FROM VENDEDOR WHERE ID_VENDEDOR = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert(
                pnId                    OUT VENDEDOR.ID_VENDEDOR%TYPE,
                pvApellido              IN  VENDEDOR.APELLIDO%TYPE,
                pvDomicilio             IN  VENDEDOR.DOMICILIO%TYPE,
                pvTelefono              IN  VENDEDOR.TELEFONO%TYPE,
                pvEmail                 IN  VENDEDOR.EMAIL%TYPE,
                pdFechaComienzo         IN  VENDEDOR.FECHA_COMIENZO%TYPE,
                pvExperiencia           IN  VENDEDOR.EXPERIENCIA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_COMPRADOR.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_VENDEDOR.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into VENDEDOR
            (ID_VENDEDOR, 
             APELLIDO, 
             DOMICILIO, 
             TELEFONO, 
             EMAIL,
             FECHA_COMIENZO,
             EXPERIENCIA)
           Values
            (pnId,
             pvApellido,
             pvDomicilio,
             pvTelefono,
             pvEmail,
             pdFechaComienzo,
             pvExperiencia);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnId                    IN  VENDEDOR.ID_VENDEDOR%TYPE,
                pvApellido              IN  VENDEDOR.APELLIDO%TYPE,
                pvDomicilio             IN  VENDEDOR.DOMICILIO%TYPE,
                pvTelefono              IN  VENDEDOR.TELEFONO%TYPE,
                pvEmail                 IN  VENDEDOR.EMAIL%TYPE,
                pdFechaComienzo         IN  VENDEDOR.FECHA_COMIENZO%TYPE,
                pvExperiencia           IN  VENDEDOR.EXPERIENCIA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update VENDEDOR
            set APELLIDO        = pvApellido, 
                DOMICILIO       = pvDomicilio, 
                TELEFONO        = pvTelefono, 
                EMAIL           = pvEmail,
                FECHA_COMIENZO  = pdFechaComienzo,
                EXPERIENCIA     = pvExperiencia
            where ID_VENDEDOR = pnId;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnId                    IN  VENDEDOR.ID_VENDEDOR%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from VENDEDOR where ID_VENDEDOR = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_VENDEDOR; 
/

