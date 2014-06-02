--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.1
-- Started on 2014-06-02 12:18:54

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 180 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2020 (class 0 OID 0)
-- Dependencies: 180
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 49284)
-- Name: adresgegevens; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE adresgegevens (
    id integer NOT NULL,
    straat character varying NOT NULL,
    huisnummer integer NOT NULL,
    postcode character varying NOT NULL,
    woonplaats character varying NOT NULL,
    telefoon_nummer character varying NOT NULL,
    CONSTRAINT docent_postcode_check CHECK (((postcode)::text ~ similar_escape('[0-9]{4}[A-Z]{2}'::text, NULL::text))),
    CONSTRAINT docent_telefoon_nummer_check CHECK (((telefoon_nummer)::text ~ similar_escape('(\d|\s|\.|\(|\)|-|)*'::text, NULL::text)))
);


ALTER TABLE public.adresgegevens OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 49292)
-- Name: adresgegevens_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE adresgegevens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.adresgegevens_id_seq OWNER TO postgres;

--
-- TOC entry 2021 (class 0 OID 0)
-- Dependencies: 171
-- Name: adresgegevens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE adresgegevens_id_seq OWNED BY adresgegevens.id;


--
-- TOC entry 172 (class 1259 OID 49294)
-- Name: docent; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE docent (
    medewerkerscode character varying NOT NULL,
    voornaam character varying NOT NULL,
    tussenvoegsel character varying DEFAULT ''::character varying NOT NULL,
    achternaam character varying NOT NULL,
    adres_id integer NOT NULL,
    geslacht character varying NOT NULL,
    CONSTRAINT docent_geslacht_check CHECK (((geslacht)::text = ANY (ARRAY[('man'::character varying)::text, ('vrouw'::character varying)::text, ('onbepaald'::character varying)::text, ('onbekend'::character varying)::text])))
);


ALTER TABLE public.docent OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 49302)
-- Name: klas; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE klas (
    klascode character varying NOT NULL,
    start_datum date NOT NULL,
    eind_datum date NOT NULL,
    CONSTRAINT klas_datum_check CHECK ((eind_datum > start_datum))
);


ALTER TABLE public.klas OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 49309)
-- Name: klas_studenten; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE klas_studenten (
    id integer NOT NULL,
    klas_id character varying NOT NULL,
    student_id character varying NOT NULL
);


ALTER TABLE public.klas_studenten OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 49315)
-- Name: klas_studenten_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE klas_studenten_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.klas_studenten_id_seq OWNER TO postgres;

--
-- TOC entry 2022 (class 0 OID 0)
-- Dependencies: 175
-- Name: klas_studenten_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE klas_studenten_id_seq OWNED BY klas_studenten.id;


--
-- TOC entry 176 (class 1259 OID 49317)
-- Name: modules; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE modules (
    modulecode character varying NOT NULL,
    modulebeheerder character varying NOT NULL,
    start_datum date NOT NULL,
    eind_datum date NOT NULL,
    CONSTRAINT module_date_check CHECK ((eind_datum > start_datum)),
    CONSTRAINT modulecode_type CHECK (((modulecode)::text ~ similar_escape('[A-Z]+'::text, NULL::text)))
);


ALTER TABLE public.modules OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 49325)
-- Name: rooster; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rooster (
    id integer NOT NULL,
    lokaal character varying NOT NULL,
    start_tijd timestamp without time zone NOT NULL,
    eind_tijd timestamp without time zone NOT NULL,
    klas character varying NOT NULL,
    docent character varying NOT NULL,
    module character varying NOT NULL,
    CONSTRAINT rooster_tijd CHECK ((eind_tijd > start_tijd))
);


ALTER TABLE public.rooster OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 49332)
-- Name: rooster_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rooster_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rooster_id_seq OWNER TO postgres;

--
-- TOC entry 2023 (class 0 OID 0)
-- Dependencies: 178
-- Name: rooster_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rooster_id_seq OWNED BY rooster.id;


--
-- TOC entry 179 (class 1259 OID 49334)
-- Name: student; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE student (
    studentnummer character varying NOT NULL,
    voornaam character varying DEFAULT ''::character varying NOT NULL,
    tussenvoegsel character varying DEFAULT ''::character varying,
    achternaam character varying DEFAULT ''::character varying NOT NULL,
    geslacht character varying NOT NULL,
    adres_id integer NOT NULL,
    CONSTRAINT student_geslacht_check CHECK (((geslacht)::text = ANY (ARRAY[('man'::character varying)::text, ('vrouw'::character varying)::text, ('onbepaald'::character varying)::text, ('onbekend'::character varying)::text]))),
    CONSTRAINT student_studentnummer_check CHECK ((length((studentnummer)::text) = 7))
);


ALTER TABLE public.student OWNER TO postgres;

--
-- TOC entry 1858 (class 2604 OID 49345)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY adresgegevens ALTER COLUMN id SET DEFAULT nextval('adresgegevens_id_seq'::regclass);


--
-- TOC entry 1864 (class 2604 OID 49346)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY klas_studenten ALTER COLUMN id SET DEFAULT nextval('klas_studenten_id_seq'::regclass);


--
-- TOC entry 1867 (class 2604 OID 49347)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rooster ALTER COLUMN id SET DEFAULT nextval('rooster_id_seq'::regclass);


--
-- TOC entry 2024 (class 0 OID 0)
-- Dependencies: 171
-- Name: adresgegevens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('adresgegevens_id_seq', 24, true);



--
-- TOC entry 2025 (class 0 OID 0)
-- Dependencies: 175
-- Name: klas_studenten_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('klas_studenten_id_seq', 48, true);


--
-- TOC entry 2026 (class 0 OID 0)
-- Dependencies: 178
-- Name: rooster_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('rooster_id_seq', 53, true);


--
-- TOC entry 1875 (class 2606 OID 49349)
-- Name: adresgegevens_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY adresgegevens
    ADD CONSTRAINT adresgegevens_id_key UNIQUE (id);


--
-- TOC entry 1877 (class 2606 OID 49351)
-- Name: pk_docent; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY docent
    ADD CONSTRAINT pk_docent PRIMARY KEY (medewerkerscode);


--
-- TOC entry 1879 (class 2606 OID 49353)
-- Name: pk_klas; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY klas
    ADD CONSTRAINT pk_klas PRIMARY KEY (klascode);


--
-- TOC entry 1881 (class 2606 OID 49355)
-- Name: pk_klas_studenten; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY klas_studenten
    ADD CONSTRAINT pk_klas_studenten PRIMARY KEY (id);


--
-- TOC entry 1883 (class 2606 OID 49357)
-- Name: pk_modulecode; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY modules
    ADD CONSTRAINT pk_modulecode PRIMARY KEY (modulecode);


--
-- TOC entry 1885 (class 2606 OID 49359)
-- Name: pk_rooster; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rooster
    ADD CONSTRAINT pk_rooster PRIMARY KEY (id);


--
-- TOC entry 1887 (class 2606 OID 49361)
-- Name: pk_student; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY student
    ADD CONSTRAINT pk_student PRIMARY KEY (studentnummer);


--
-- TOC entry 1888 (class 2606 OID 49362)
-- Name: docent_adres_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY docent
    ADD CONSTRAINT docent_adres_id_fkey FOREIGN KEY (adres_id) REFERENCES adresgegevens(id) ON DELETE CASCADE;


--
-- TOC entry 1890 (class 2606 OID 49367)
-- Name: klas_studenten_klas_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY klas_studenten
    ADD CONSTRAINT klas_studenten_klas_id_fkey FOREIGN KEY (klas_id) REFERENCES klas(klascode) ON DELETE CASCADE;


--
-- TOC entry 1889 (class 2606 OID 49372)
-- Name: klas_studenten_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY klas_studenten
    ADD CONSTRAINT klas_studenten_student_id_fkey FOREIGN KEY (student_id) REFERENCES student(studentnummer) ON DELETE CASCADE;


--
-- TOC entry 1891 (class 2606 OID 49377)
-- Name: modules_modulebeheerder_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY modules
    ADD CONSTRAINT modules_modulebeheerder_fkey FOREIGN KEY (modulebeheerder) REFERENCES docent(medewerkerscode) ON DELETE CASCADE;


--
-- TOC entry 1894 (class 2606 OID 49382)
-- Name: rooster_docent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rooster
    ADD CONSTRAINT rooster_docent_fkey FOREIGN KEY (docent) REFERENCES docent(medewerkerscode) ON DELETE CASCADE;


--
-- TOC entry 1893 (class 2606 OID 49387)
-- Name: rooster_klas_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rooster
    ADD CONSTRAINT rooster_klas_fkey FOREIGN KEY (klas) REFERENCES klas(klascode) ON DELETE CASCADE;


--
-- TOC entry 1892 (class 2606 OID 49392)
-- Name: rooster_module_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rooster
    ADD CONSTRAINT rooster_module_fkey FOREIGN KEY (module) REFERENCES modules(modulecode) ON DELETE CASCADE;


--
-- TOC entry 1895 (class 2606 OID 49397)
-- Name: student_adres_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY student
    ADD CONSTRAINT student_adres_id_fkey FOREIGN KEY (adres_id) REFERENCES adresgegevens(id) ON DELETE CASCADE;


--
-- TOC entry 2019 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2014-06-02 12:18:55

--
-- PostgreSQL database dump complete
--

