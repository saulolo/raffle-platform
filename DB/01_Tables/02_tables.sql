/*
* Saul Echeverri
* 23-05-2024
* Sprint #1
* Archivo Version Incial, esta version es creada con el fin de llevar control de la version de los scripts de BD
* Manual de Uso:
* 1. Abrir un espacio de comentarios entre corchetes
* 2. Definir el Usuario que realiza la modificación
* 3. Fecha en la cual se realiza modificación
* 4. Detalle o definicion de la modificación
* 5. Anexar script para ser ejecutado
* 6. Generar un Pull Request siguiendo el proceso establecido para actualización de ramas
*/

/*
* Yency Serrano
* 23-05-2024
* Se agrega la Base de Datos: raffle_platform_db con las tablas:
* customers
* permissions
* raffles
* roles
* roles_permissions
* users
* users_roles
*/


CREATE TABLE raffle.customers
(
    id         bigint NOT NULL,
    cell_phone character varying(255),
    email      character varying(255),
    full_name  character varying(255),
    raffle_id  bigint
);
COMMENT
ON TABLE raffle.customers IS 'Tabla que registra la información de las personas que compran boletas ';
COMMENT
ON COLUMN raffle.customers.id IS 'Identificador de la tabla customers';
COMMENT
ON COLUMN raffle.customers.cell_phone IS 'Celular del comprador de la boleta';
COMMENT
ON COLUMN raffle.customers.email IS 'Correo electronico del comprador';
COMMENT
ON COLUMN raffle.customers.full_name IS 'Nombre completo del comprador';


ALTER TABLE raffle.customers OWNER TO doadmin;

CREATE SEQUENCE raffle.customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE raffle.customers_id_seq OWNER TO doadmin;


ALTER SEQUENCE raffle.customers_id_seq OWNED BY raffle.customers.id;



CREATE TABLE raffle.permissions
(
    id   bigint                 NOT NULL,
    name character varying(255) NOT NULL
);

COMMENT
ON TABLE raffle.permissions IS 'Se almacena los permisos para acceder a las opciones ';
COMMENT
ON COLUMN raffle.permissions.id IS 'Identificador de la tabla Permissions';
COMMENT
ON COLUMN raffle.permissions.name IS 'Nombre del permiso';


ALTER TABLE raffle.permissions OWNER TO doadmin;


CREATE SEQUENCE raffle.permissions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE raffle.permissions_id_seq OWNER TO doadmin;


ALTER SEQUENCE raffle.permissions_id_seq OWNED BY raffle.permissions.id;


CREATE TABLE raffle.raffles
(
    id                bigint NOT NULL,
    creation_date     timestamp(6) without time zone,
    description       character varying(255),
    image             character varying(255),
    is_donated_prize  boolean,
    name              character varying(255),
    number_of_figures integer,
    prize_description character varying(255),
    prize_value       integer,
    raffle_date       timestamp(6) without time zone,
    status            character varying(255),
    ticket_price      integer,
    winner_number     integer,
    CONSTRAINT raffles_status_check CHECK (((status)::text = ANY ((ARRAY['ACTIVE':: character varying, 'INACTIVE':: character varying])::text[])
) )
);


COMMENT
ON TABLE raffle.raffles IS 'Registra las rifas que hace la fundación ';
COMMENT
ON COLUMN raffle.raffles.id IS 'Identificador de la tabla Raffles';
COMMENT
ON COLUMN raffle.raffles.name IS 'Identificador de la tabla Raffles';
COMMENT
ON COLUMN raffle.raffles.creation_date IS 'Fecha de creacion';
COMMENT
ON COLUMN raffle.raffles.description IS 'Descripcion de la rifa';
COMMENT
ON COLUMN raffle.raffles.image IS 'Imagen'; --ojo por eliminar
COMMENT
ON COLUMN raffle.raffles.is_donated_prize IS 'El premio es donado? 1-Si o 0-No ';
COMMENT
ON COLUMN raffle.raffles.number_of_figures IS 'Numero de boletas';
COMMENT
ON COLUMN raffle.raffles.prize_description IS 'Descripcion del premio';
COMMENT
ON COLUMN raffle.raffles.prize_value IS 'Valor del premio';
COMMENT
ON COLUMN raffle.raffles.raffle_date IS 'Fecha del sorteo';
COMMENT
ON COLUMN raffle.raffles.status IS 'Estado: ACTIVA, INACTIVA, ELIMINADA, FINALIZADA';
COMMENT
ON COLUMN raffle.raffles.ticket_price IS 'Precio de la boleta';
COMMENT
ON COLUMN raffle.raffles.winner_number IS 'Numero Ganador';



ALTER TABLE raffle.raffles OWNER TO doadmin;



CREATE SEQUENCE raffle.raffles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE raffle.raffles_id_seq OWNER TO doadmin;


ALTER SEQUENCE raffle.raffles_id_seq OWNED BY raffle.raffles.id;



CREATE TABLE raffle.roles
(
    id        bigint NOT NULL,
    role_name character varying(255),
    CONSTRAINT roles_role_name_check CHECK (((role_name)::text = ANY ((ARRAY['ADMIN':: character varying, 'VOLUNTEER':: character varying])::text[])
) )
);



COMMENT
ON TABLE raffle.roles IS 'Registra los roles de la aplicacion';
COMMENT
ON COLUMN raffle.roles.id IS 'Identificador de la tabla Roles';
COMMENT
ON COLUMN raffle.roles.role_name IS 'Nombre del rol';



ALTER TABLE raffle.roles OWNER TO doadmin;



CREATE SEQUENCE raffle.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE raffle.roles_id_seq OWNER TO doadmin;


ALTER SEQUENCE raffle.roles_id_seq OWNED BY raffle.roles.id;


CREATE TABLE raffle.roles_permissions
(
    role_id       bigint NOT NULL,
    permission_id bigint NOT NULL
);
COMMENT
ON TABLE raffle.roles_permissions IS 'Almacena la relacion de roles y permisos de la aplicacion';
COMMENT
ON COLUMN raffle.roles_permissions.role_id IS 'Identificador de la tabla rol';
COMMENT
ON COLUMN raffle.roles_permissions.permission_id IS 'Nombre del permissions';


ALTER TABLE raffle.roles_permissions OWNER TO doadmin;



CREATE TABLE raffle.users
(
    id                         bigint NOT NULL,
    cell_phone                 character varying(255),
    document_number            character varying(255),
    document_type              character varying(255),
    email                      character varying(255),
    full_name                  character varying(255),
    is_account_non_expired     boolean,
    is_account_non_locked      boolean,
    is_credentials_non_expired boolean,
    is_enabled                 boolean,
    password                   character varying(255),
    username                   character varying(255)
);

COMMENT
ON TABLE raffle.users IS 'Tabla encargada de registrar los usuarios de la aplicacion';
COMMENT
ON COLUMN raffle.users.id IS 'Identificador de la tabla users';
COMMENT
ON COLUMN raffle.users.cell_phone IS 'Telefono celular';
COMMENT
ON COLUMN raffle.users.document_number IS 'Nro de documento del usuario';
COMMENT
ON COLUMN raffle.users.document_type IS 'Tipo de documento';
COMMENT
ON COLUMN raffle.users.email IS 'Correo electronico';
COMMENT
ON COLUMN raffle.users.full_name IS 'Nombres y apellidos';
COMMENT
ON COLUMN raffle.users.is_account_non_expired IS 'La cuenta no esta vencida';
COMMENT
ON COLUMN raffle.users.is_account_non_locked IS 'La cuenta no esta bloqueada';
COMMENT
ON COLUMN raffle.users.is_credentials_non_expired IS 'Credenciales no han expirado';
COMMENT
ON COLUMN raffle.users.is_enabled IS 'Usuario Esta habilitado';
COMMENT
ON COLUMN raffle.users.password IS 'Contraseña del usuario';
COMMENT
ON COLUMN raffle.users.username IS 'Nombre de usuario';

ALTER TABLE raffle.users OWNER TO doadmin;



CREATE SEQUENCE raffle.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE raffle.users_id_seq OWNER TO doadmin;


CREATE SEQUENCE raffle.users_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER SEQUENCE raffle.users_id_seq1 OWNER TO doadmin;


ALTER SEQUENCE raffle.users_id_seq1 OWNED BY raffle.users.id;

CREATE TABLE raffle.users_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);



COMMENT
ON TABLE raffle.users_roles IS 'Tabla encargada de registrar los roles que tienen los usuarios';
COMMENT
ON COLUMN raffle.users_roles.user_id IS 'Identificador de la tabla users';
COMMENT
ON COLUMN raffle.users_roles.role_id IS 'Identificador de la tabla roles';


ALTER TABLE raffle.users_roles OWNER TO doadmin;


ALTER TABLE ONLY raffle.customers ALTER COLUMN id SET DEFAULT nextval('raffle.customers_id_seq'::regclass);


ALTER TABLE ONLY raffle.permissions ALTER COLUMN id SET DEFAULT nextval('raffle.permissions_id_seq'::regclass);


ALTER TABLE ONLY raffle.raffles ALTER COLUMN id SET DEFAULT nextval('raffle.raffles_id_seq'::regclass);


ALTER TABLE ONLY raffle.roles ALTER COLUMN id SET DEFAULT nextval('raffle.roles_id_seq'::regclass);


ALTER TABLE ONLY raffle.users ALTER COLUMN id SET DEFAULT nextval('raffle.users_id_seq1'::regclass);


SELECT pg_catalog.setval('raffle.customers_id_seq', 1, false);


SELECT pg_catalog.setval('raffle.permissions_id_seq', 4, true);


SELECT pg_catalog.setval('raffle.raffles_id_seq', 1, false);


SELECT pg_catalog.setval('raffle.roles_id_seq', 2, true);


SELECT pg_catalog.setval('raffle.users_id_seq', 1, true);


SELECT pg_catalog.setval('raffle.users_id_seq1', 6, true);


ALTER TABLE ONLY raffle.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);



ALTER TABLE ONLY raffle.permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);



ALTER TABLE ONLY raffle.raffles
    ADD CONSTRAINT raffles_pkey PRIMARY KEY (id);


ALTER TABLE ONLY raffle.roles_permissions
    ADD CONSTRAINT roles_permissions_pkey PRIMARY KEY (role_id, permission_id);


ALTER TABLE ONLY raffle.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


ALTER TABLE ONLY raffle.permissions
    ADD CONSTRAINT uk_pnvtwliis6p05pn6i3ndjrqt2 UNIQUE (name);


ALTER TABLE ONLY raffle.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


ALTER TABLE ONLY raffle.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


ALTER TABLE ONLY raffle.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);


ALTER TABLE ONLY raffle.users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES raffle.users(id);


ALTER TABLE ONLY raffle.roles_permissions
    ADD CONSTRAINT fkbx9r9uw77p58gsq4mus0mec0o FOREIGN KEY (permission_id) REFERENCES raffle.permissions(id);


ALTER TABLE ONLY raffle.customers
    ADD CONSTRAINT fkdgheeka8bbb99dyxqx0waiqpr FOREIGN KEY (raffle_id) REFERENCES raffle.raffles(id);


ALTER TABLE ONLY raffle.users_roles
    ADD CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES raffle.roles(id);


ALTER TABLE ONLY raffle.roles_permissions
    ADD CONSTRAINT fkqi9odri6c1o81vjox54eedwyh FOREIGN KEY (role_id) REFERENCES raffle.roles(id);

/*
* Saul Echeverri
* 11-05-2024
* Se agrega a la Base de Datos la tabla: 'tickets'
*/
CREATE TABLE raffle.tickets
(
    id               bigserial    NOT NULL,
    "comments"       varchar(200) NULL,
    payment_file     oid NULL ,
    purchase_date    timestamp(6) null,
    reservation_date timestamp(6) NULL ,
    status           varchar(255) NOT null,
    ticket_number    int4 NULL ,
    customer_id      int8 NULL ,
    raffle_id        int8 NULL ,
    user_id          int8 NULL ,
    CONSTRAINT tickets_pkey PRIMARY KEY (id),
    CONSTRAINT tickets_status_check CHECK (((status)::text = ANY ((ARRAY['AVAILABLE':: character varying, 'RESERVED':: character varying, 'PAID':: character varying])::text[])
) )
);

ALTER TABLE raffle.tickets
    ADD CONSTRAINT USER_ID_FK FOREIGN KEY (user_id) REFERENCES raffle.users (id);
ALTER TABLE raffle.tickets
    ADD CONSTRAINT RAFFLE_ID_FK FOREIGN KEY (raffle_id) REFERENCES raffle.raffles (id);
ALTER TABLE raffle.tickets
    ADD CONSTRAINT CUSTOMER_ID_FK FOREIGN KEY (customer_id) REFERENCES raffle.customers (id);


comment ON TABLE raffle.tickets IS 'Tabla encargada de registrar las boletas asociadas a la rifa';
comment ON COLUMN raffle.tickets."comments" is 'Comentarios';
comment ON COLUMN raffle.tickets.payment_file IS 'Archivo de pago adjunto a la boleta';
comment ON COLUMN raffle.tickets.purchase_date IS 'Fecha de compra de la boleta';
comment ON COLUMN raffle.tickets.reservation_date IS 'Fecha de reserva de la boleta';
comment ON COLUMN raffle.tickets.status IS 'Estado de la boleta (disponible, reservada, pagada)';
comment ON COLUMN raffle.tickets.ticket_number IS 'Número de la boleta';
comment ON COLUMN raffle.tickets.customer_id IS 'ID del comprador de la boleta';
comment ON COLUMN raffle.tickets.raffle_id IS 'ID del rifa asociada a la boleta';
comment ON COLUMN raffle.tickets.user_id IS 'ID del usuario voluntario asociado a la boleta';