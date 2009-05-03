CREATE OR REPLACE PACKAGE TP1_BD.PQ_PROPIETARIO IS

FUNCTION f_exist_id(
                pnId        IN PROPIETARIO.ID_PROPIETARIO%TYPE
                ) RETURN BOOLEAN;

PROCEDURE sp_insert_prop_empresa(
                pnId                    OUT PROPIETARIO.ID_PROPIETARIO%TYPE,
                pvDomicilio             IN  PROPIETARIO.DOMICILIO%TYPE,
                pvTelefono              IN  PROPIETARIO.TELEFONO%TYPE,
                pvEmail                 IN  PROPIETARIO.EMAIL%TYPE,
                pvCuit                  IN  PROPIETARIO_EMPRESA.CUIT%TYPE,
                pvRazonSocial           IN  PROPIETARIO_EMPRESA.RAZON_SOCIAL%TYPE,
                pdFechaComienzoAct      IN  PROPIETARIO_EMPRESA.FECHA_COMIENZO_ACTIVIDADES%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_insert_prop_particular(
                pnId                    OUT PROPIETARIO.ID_PROPIETARIO%TYPE,
                pvDomicilio             IN  PROPIETARIO.DOMICILIO%TYPE,
                pvTelefono              IN  PROPIETARIO.TELEFONO%TYPE,
                pvEmail                 IN  PROPIETARIO.EMAIL%TYPE,
                pvApellido              IN  PROPIETARIO_PARTICULAR.APELLIDO%TYPE,
                pvNombre                IN  PROPIETARIO_PARTICULAR.NOMBRE%TYPE,
                pvDocumento             IN  PROPIETARIO_PARTICULAR.DOCUMENTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update_prop_empresa(
                pnId                    IN  PROPIETARIO.ID_PROPIETARIO%TYPE,
                pvDomicilio             IN  PROPIETARIO.DOMICILIO%TYPE,
                pvTelefono              IN  PROPIETARIO.TELEFONO%TYPE,
                pvEmail                 IN  PROPIETARIO.EMAIL%TYPE,
                pvCuit                  IN  PROPIETARIO_EMPRESA.CUIT%TYPE,
                pvRazonSocial           IN  PROPIETARIO_EMPRESA.RAZON_SOCIAL%TYPE,
                pdFechaComienzoAct      IN  PROPIETARIO_EMPRESA.FECHA_COMIENZO_ACTIVIDADES%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update_prop_particular(
                pnId                    IN  PROPIETARIO.ID_PROPIETARIO%TYPE,
                pvDomicilio             IN  PROPIETARIO.DOMICILIO%TYPE,
                pvTelefono              IN  PROPIETARIO.TELEFONO%TYPE,
                pvEmail                 IN  PROPIETARIO.EMAIL%TYPE,
                pvApellido              IN  PROPIETARIO_PARTICULAR.APELLIDO%TYPE,
                pvNombre                IN  PROPIETARIO_PARTICULAR.NOMBRE%TYPE,
                pvDocumento             IN  PROPIETARIO_PARTICULAR.DOCUMENTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnId                    IN  PROPIETARIO.ID_PROPIETARIO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_PROPIETARIO; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_PROPIETARIO IS

FUNCTION f_exist_id(
                pnId        IN PROPIETARIO.ID_PROPIETARIO%TYPE
                ) RETURN BOOLEAN IS

        vID      PROPIETARIO.ID_PROPIETARIO%TYPE;                
    BEGIN
        SELECT ID_PROPIETARIO INTO vID FROM PROPIETARIO WHERE ID_PROPIETARIO = pnId;
        RETURN TRUE;

  EXCEPTION
    WHEN OTHERS THEN
        RETURN FALSE;
END f_exist_id;                



PROCEDURE sp_insert_prop_empresa(
                pnId                    OUT PROPIETARIO.ID_PROPIETARIO%TYPE,
                pvDomicilio             IN  PROPIETARIO.DOMICILIO%TYPE,
                pvTelefono              IN  PROPIETARIO.TELEFONO%TYPE,
                pvEmail                 IN  PROPIETARIO.EMAIL%TYPE,
                pvCuit                  IN  PROPIETARIO_EMPRESA.CUIT%TYPE,
                pvRazonSocial           IN  PROPIETARIO_EMPRESA.RAZON_SOCIAL%TYPE,
                pdFechaComienzoAct      IN  PROPIETARIO_EMPRESA.FECHA_COMIENZO_ACTIVIDADES%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_PROPIETARIO.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_PROPIETARIO.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into PROPIETARIO
            (ID_PROPIETARIO, 
             DOMICILIO, 
             TELEFONO, 
             EMAIL,
             TIPO)
          Values
            (pnId,
             pvDomicilio,
             pvTelefono,
             pvEmail,
             'Empresa');
             
        Insert into PROPIETARIO_EMPRESA
            (ID_PROPIETARIO, 
             CUIT, 
             RAZON_SOCIAL, 
             FECHA_COMIENZO_ACTIVIDADES)
          Values
            (pnId,
             pvCuit,
             pvRazonSocial,
             pdFechaComienzoAct);
             

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert_prop_empresa;                

PROCEDURE sp_insert_prop_particular(
                pnId                    OUT PROPIETARIO.ID_PROPIETARIO%TYPE,
                pvDomicilio             IN  PROPIETARIO.DOMICILIO%TYPE,
                pvTelefono              IN  PROPIETARIO.TELEFONO%TYPE,
                pvEmail                 IN  PROPIETARIO.EMAIL%TYPE,
                pvApellido              IN  PROPIETARIO_PARTICULAR.APELLIDO%TYPE,
                pvNombre                IN  PROPIETARIO_PARTICULAR.NOMBRE%TYPE,
                pvDocumento             IN  PROPIETARIO_PARTICULAR.DOCUMENTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

        vEXIST      BOOLEAN;                
    BEGIN
        SELECT SEQ_PROPIETARIO.NEXTVAL INTO pnId FROM DUAL;

        LOOP
            vEXIST := F_EXIST_ID(pnId);
            EXIT WHEN NOT vEXIST;
            SELECT SEQ_PROPIETARIO.NEXTVAL INTO pnId FROM DUAL;
        END LOOP;

        Insert into PROPIETARIO
            (ID_PROPIETARIO, 
             DOMICILIO, 
             TELEFONO, 
             EMAIL,
             TIPO)
          Values
            (pnId,
             pvDomicilio,
             pvTelefono,
             pvEmail,
             'Particular');
             
        Insert into PROPIETARIO_PARTICULAR
            (ID_PROPIETARIO, 
             APELLIDO, 
             NOMBRE, 
             DOCUMENTO)
          Values
            (pnId,
             pvApellido,
             pvNombre,
             pvDocumento);
             

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert_prop_particular;                

PROCEDURE sp_update_prop_empresa(
                pnId                    IN  PROPIETARIO.ID_PROPIETARIO%TYPE,
                pvDomicilio             IN  PROPIETARIO.DOMICILIO%TYPE,
                pvTelefono              IN  PROPIETARIO.TELEFONO%TYPE,
                pvEmail                 IN  PROPIETARIO.EMAIL%TYPE,
                pvCuit                  IN  PROPIETARIO_EMPRESA.CUIT%TYPE,
                pvRazonSocial           IN  PROPIETARIO_EMPRESA.RAZON_SOCIAL%TYPE,
                pdFechaComienzoAct      IN  PROPIETARIO_EMPRESA.FECHA_COMIENZO_ACTIVIDADES%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update PROPIETARIO
            set DOMICILIO       = pvDomicilio, 
                TELEFONO        = pvTelefono, 
                EMAIL           = pvEmail
            where ID_PROPIETARIO = pnId;

        update PROPIETARIO_EMPRESA
            set CUIT            = pvCuit,
                RAZON_SOCIAL    = pvRazonSocial,
                FECHA_COMIENZO_ACTIVIDADES  = pdFechaComienzoAct
            where ID_PROPIETARIO = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update_prop_empresa;                

PROCEDURE sp_update_prop_particular(
                pnId                    IN  PROPIETARIO.ID_PROPIETARIO%TYPE,
                pvDomicilio             IN  PROPIETARIO.DOMICILIO%TYPE,
                pvTelefono              IN  PROPIETARIO.TELEFONO%TYPE,
                pvEmail                 IN  PROPIETARIO.EMAIL%TYPE,
                pvApellido              IN  PROPIETARIO_PARTICULAR.APELLIDO%TYPE,
                pvNombre                IN  PROPIETARIO_PARTICULAR.NOMBRE%TYPE,
                pvDocumento             IN  PROPIETARIO_PARTICULAR.DOCUMENTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update PROPIETARIO
            set DOMICILIO       = pvDomicilio, 
                TELEFONO        = pvTelefono, 
                EMAIL           = pvEmail
            where ID_PROPIETARIO = pnId;

        update PROPIETARIO_PARTICULAR
            set APELLIDO    = pvApellido,
                NOMBRE      = pvNombre,
                DOCUMENTO   = pvDocumento
            where ID_PROPIETARIO = pnId;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update_prop_particular;                

PROCEDURE sp_delete(
                pnId                    IN  PROPIETARIO.ID_PROPIETARIO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN
    
        delete from PROPIETARIO_EMPRESA WHERE ID_PROPIETARIO = pnId;
        delete from PROPIETARIO_PARTICULAR WHERE ID_PROPIETARIO = pnId;
        delete from PROPIETARIO where ID_PROPIETARIO = pnId;
    
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_PROPIETARIO; 
/

