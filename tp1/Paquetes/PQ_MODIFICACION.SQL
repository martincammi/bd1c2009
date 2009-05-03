CREATE OR REPLACE PACKAGE TP1_BD.PQ_MODIFICACION IS

PROCEDURE sp_insert(
                pnIdContrato            IN  MODIFICACION.ID_CONTRATO%TYPE,
                pnIdTexto               IN  MODIFICACION.ID_TEXTO%TYPE,
                pdFechaInicio           IN  MODIFICACION.FECHA_INICIO%TYPE,
                pdFechaFin              IN  MODIFICACION.FECHA_FIN%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnIdContrato            IN  MODIFICACION.ID_CONTRATO%TYPE,
                pnIdTexto               IN  MODIFICACION.ID_TEXTO%TYPE,
                pdFechaInicio           IN  MODIFICACION.FECHA_INICIO%TYPE,
                pdFechaFin              IN  MODIFICACION.FECHA_FIN%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnIdContrato            IN  MODIFICACION.ID_CONTRATO%TYPE,
                pnIdTexto               IN  MODIFICACION.ID_TEXTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_MODIFICACION; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_MODIFICACION IS



PROCEDURE sp_insert(
                pnIdContrato            IN  MODIFICACION.ID_CONTRATO%TYPE,
                pnIdTexto               IN  MODIFICACION.ID_TEXTO%TYPE,
                pdFechaInicio           IN  MODIFICACION.FECHA_INICIO%TYPE,
                pdFechaFin              IN  MODIFICACION.FECHA_FIN%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        Insert into MODIFICACION
            (ID_CONTRATO, 
             ID_TEXTO,
             FECHA_INICIO,
             FECHA_FIN)
           Values
            (pnIdContrato,
             pnIdTexto,
             pdFechaInicio,
             pdFechaFin);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnIdContrato            IN  MODIFICACION.ID_CONTRATO%TYPE,
                pnIdTexto               IN  MODIFICACION.ID_TEXTO%TYPE,
                pdFechaInicio           IN  MODIFICACION.FECHA_INICIO%TYPE,
                pdFechaFin              IN  MODIFICACION.FECHA_FIN%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update MODIFICACION
            set FECHA_INICIO    = pdFechaInicio, 
                FECHA_FIN       = pdFechaFin
            where ID_CONTRATO   = pnIdContrato
                and ID_TEXTO    = pnIdTexto;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnIdContrato            IN  MODIFICACION.ID_CONTRATO%TYPE,
                pnIdTexto               IN  MODIFICACION.ID_TEXTO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from MODIFICACION 
            where ID_CONTRATO = pnIdContrato
                and ID_TEXTO = pnIdTexto;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_MODIFICACION; 
/

