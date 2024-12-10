CREATE OR REPLACE PACKAGE BODY manage_produkt IS
    PROCEDURE dodaj_produkt (
        p_nazwa IN PRODUKT.nazwa%TYPE,
        p_opis IN PRODUKT.opis%TYPE,
        p_cena IN PRODUKT.cena%TYPE,
        p_kategoria_nazwa IN PRODUKT.kategoria_nazwa%TYPE
    ) AS
    BEGIN
        IF p_cena <= 0 THEN
            RAISE_APPLICATION_ERROR(-20002, 'Podano niedodatnią cenę!');
        END IF;

        INSERT INTO PRODUKT(nazwa, opis, cena, kategoria_nazwa)
        VALUES (p_nazwa, p_opis, p_cena, p_kategoria_nazwa);
    END dodaj_produkt;

    PROCEDURE usun_produkt (
        p_nazwa IN PRODUKT.nazwa%TYPE
    ) AS
    BEGIN
        DELETE FROM PRODUKT WHERE nazwa = p_nazwa;
    END usun_produkt;

    PROCEDURE zmien_dane_produktu (
        p_nazwa IN PRODUKT.nazwa%TYPE,
        p_opis IN PRODUKT.opis%TYPE,
        p_cena IN PRODUKT.cena%TYPE,
        p_kategoria_nazwa IN PRODUKT.kategoria_nazwa%TYPE
    ) AS
    BEGIN

        IF p_cena <= 0 THEN
            RAISE_APPLICATION_ERROR(-20002, 'Podano niedodatnią cenę!');
        END IF;

        UPDATE PRODUKT SET
            nazwa = p_nazwa,
            opis = p_opis,
            cena = p_cena,
            kategoria_nazwa = p_kategoria_nazwa
        WHERE nazwa = p_nazwa;
    END zmien_dane_produktu;

    PROCEDURE dodaj_tag (
        p_nazwa IN PRODUKT.nazwa%TYPE,
        p_tag IN TAG.nazwa_tagu%TYPE
    ) AS
    BEGIN
        INSERT INTO HERBATA_TAG(nazwa, tag_nazwa)
        VALUES (p_nazwa, p_tag);
    END dodaj_tag;

    PROCEDURE dodaj_promocje_do_produktu (
        p_nazwa IN PRODUKT.nazwa%TYPE,
        p_nazwa_promocji IN PROMOCJA.nazwa_promocji%TYPE
    ) AS
    BEGIN
        INSERT INTO PRODUKT_NA_PROMOCJI(nazwa_produktu, nazwa_promocji)
        VALUES (p_nazwa, p_nazwa_promocji);
    END dodaj_promocje_do_produktu;

    PROCEDURE dodaj_promocje (
        p_nazwa_promocji IN PROMOCJA.nazwa_promocji%TYPE,
        p_znizka IN PROMOCJA.znizka%TYPE,
        p_data_od IN PROMOCJA.data_od%TYPE,
        p_data_do IN PROMOCJA.data_do%TYPE,
    ) AS
    BEGIN
        INSERT INTO PROMOCJA(nazwa_promocji, znizka, data_od, data_do)
        VALUES (p_nazwa_promocji, p_znizka, p_data_od, p_data_do);
    END dodaj_promocje;

    PROCEDURE dodaj_tag (
        p_nazwa_tagu IN TAG.nazwa_tagu%TYPE
    ) AS
    BEGIN
        INSERT INTO TAG(nazwa_tagu)
        VALUES (p_nazwa_tagu);
    END dodaj_promocje;

    PROCEDURE dodaj_kategorie (
        p_nazwa IN KATEGORIA.nazwa%TYPE
    ) AS
    BEGIN
        INSERT INTO KATEGORIA(nazwa)
        VALUES (p_nazwa);
    END dodaj_kategorie;

END;