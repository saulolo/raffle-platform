--Paso 1 En la instancia se debe crear la base de datos ej raffle_platform_qa
--Paso 2 crear el usuario que puede acceder a la bd ejem: raffle_qa

--Paso 3. Crear el schema (este nombre se debe mantener en cada ambiente)
CREATE SCHEMA raffle;
ALTER SCHEMA raffle OWNER TO doadmin;

--AMBIENTE DEV
-- Conceder permiso al usuario para acceder al esquema
GRANT USAGE ON SCHEMA raffle TO raffle;

-- Conceder permisos en todas las tablas actuales
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA raffle TO raffle;

-- Conceder permisos para crear nuevas tablas
GRANT CREATE ON SCHEMA raffle TO raffle;

-- Conceder permisos en todas las secuencias actuales
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA raffle TO raffle;

-- Configurar permisos predeterminados para tablas y secuencias futuras
ALTER DEFAULT PRIVILEGES IN SCHEMA raffle GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO raffle;
ALTER DEFAULT PRIVILEGES IN SCHEMA raffle GRANT USAGE, SELECT ON SEQUENCES TO raffle;

--AMBIENTE QA
-- Conceder acceso al esquema
GRANT USAGE ON SCHEMA raffle TO raffle_qa;

-- Conceder permisos en todas las tablas actuales
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA raffle TO raffle_qa;

-- Conceder permisos para crear nuevas tablas
GRANT CREATE ON SCHEMA raffle TO raffle_qa;

-- Conceder permisos en todas las secuencias actuales
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA raffle TO raffle_qa;

-- Configurar permisos predeterminados para tablas y secuencias futuras
ALTER DEFAULT PRIVILEGES IN SCHEMA raffle GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO raffle_qa;
ALTER DEFAULT PRIVILEGES IN SCHEMA raffle GRANT USAGE, SELECT ON SEQUENCES TO raffle_qa;

--Paso  4. Ejecutar scripts 02_tables