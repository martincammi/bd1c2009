CREATE OR REPLACE PACKAGE TP1_BD.PQ_SE_LO_MOSTRO IS

PROCEDURE sp_insert(
                pnIdComprador           IN  SE_LO_MOSTRO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  SE_LO_MOSTRO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  SE_LO_MOSTRO.ID_CONTRATO%TYPE,
                pdFechaDeMuestra        IN  SE_LO_MOSTRO.FECHA_DE_MUESTRA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_update(
                pnIdComprador           IN  SE_LO_MOSTRO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  SE_LO_MOSTRO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  SE_LO_MOSTRO.ID_CONTRATO%TYPE,
                pdFechaDeMuestra        IN  SE_LO_MOSTRO.FECHA_DE_MUESTRA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );

PROCEDURE sp_delete(
                pnIdComprador           IN  SE_LO_MOSTRO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  SE_LO_MOSTRO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  SE_LO_MOSTRO.ID_CONTRATO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                );



END PQ_SE_LO_MOSTRO; 
/

CREATE OR REPLACE PACKAGE BODY TP1_BD.PQ_SE_LO_MOSTRO IS



PROCEDURE sp_insert(
                pnIdComprador           IN  SE_LO_MOSTRO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  SE_LO_MOSTRO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  SE_LO_MOSTRO.ID_CONTRATO%TYPE,
                pdFechaDeMuestra        IN  SE_LO_MOSTRO.FECHA_DE_MUESTRA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        Insert into SE_LO_MOSTRO
            (ID_COMPRADOR, 
             ID_VENDEDOR,
             ID_CONTRATO,
             FECHA_DE_MUESTRA)
           Values
            (pnIdComprador,
             pnIdVendedor,
             pnIdContrato,
             pdFechaDeMuestra);

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_insert;                

PROCEDURE sp_update(
                pnIdComprador           IN  SE_LO_MOSTRO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  SE_LO_MOSTRO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  SE_LO_MOSTRO.ID_CONTRATO%TYPE,
                pdFechaDeMuestra        IN  SE_LO_MOSTRO.FECHA_DE_MUESTRA%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        update SE_LO_MOSTRO
            set FECHA_DE_MUESTRA    = pdFechaDeMuestra 
            where ID_COMPRADOR   = pnIdComprador
                and ID_VENDEDOR = pnIdVendedor
                and ID_CONTRATO = pnIdContrato;

        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_update;                

PROCEDURE sp_delete(
                pnIdComprador           IN  SE_LO_MOSTRO.ID_COMPRADOR%TYPE,
                pnIdVendedor            IN  SE_LO_MOSTRO.ID_VENDEDOR%TYPE,
                pnIdContrato            IN  SE_LO_MOSTRO.ID_CONTRATO%TYPE,
                pn_err_code_out         OUT INTEGER,
                pv_err_msg_out          OUT VARCHAR2
                ) IS

    BEGIN

        delete from SE_LO_MOSTRO 
            where ID_COMPRADOR = pnIdComprador
                and ID_VENDEDOR = pnIdVendedor
                and ID_CONTRATO = pnIdContrato;
        pn_err_code_out := 0;

  EXCEPTION
    WHEN OTHERS THEN
      ROLLBACK;
      pn_err_code_out := SQLCODE;
      pv_err_msg_out  := SUBSTR(SQLERRM, 1, 250);
END sp_delete;                



END PQ_SE_LO_MOSTRO; 
/

