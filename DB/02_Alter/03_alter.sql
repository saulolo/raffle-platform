/*
* Saul Echeverri
* 04-06-2024
* Sprint #2
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
* Saul Echeverri
* 04-06-2024
* Se otorga permsisos de alter a la tabla raffles al usuario raffle
* Se agrega el campo created_by a la tabla raffles
*/
GRANT
ALTER
ON TABLE raffles TO raffle;
ALTER TABLE raffles
    ADD COLUMN created_by bigint;
COMMENT
ON COLUMN raffles.created_by IS 'Usuario creador de la rifa';


/*
* Saul Echeverri
* 06-06-2024
* Eliminación de la restricción existente y creación de una nueva que incluye el valor `DELETED` y 'FINISHED' en la
* columna `status` de la tabla `raffles`.
*/
ALTER TABLE raffle.raffles DROP CONSTRAINT raffles_status_check;
COMMENT
ON CONSTRAINT raffles_status_check ON raffle.raffles IS 'Se eliminó la restricción existente para ' ||
'actualizar los valores permitidos en la columna status';

ALTER TABLE raffle.raffles
    ADD CONSTRAINT raffles_status_check CHECK (status IN ('ACTIVE', 'INACTIVE', 'DELETED', 'FINISHED'));
COMMENT
ON CONSTRAINT raffles_status_check ON raffle.raffles IS 'Restricción actualizada para incluir el valor ' ||
'DELETED y FINISHED en la columna status';


/*
* Saul Echeverri
* 10-06-2024
* Se elimina la relación entre las tablas `raffles` y `customers`.
*/
COMMENT
ON COLUMN customers.raffle_id IS 'Se va a eliminar la relación entre las tablas raffles y customers';
alter table customers drop column raffle_id;


/*
* Saul Echeverri
* 11-06-2024
* Se agregan las relaciones entre las tablas `tickets` y `users` y entre las tablas `raffles` y `users`.
*/
-- Agregar restricciones de clave externa para las nuevas columnas
ALTER TABLE raffle.tickets
    ADD CONSTRAINT fk_user_id_tickets FOREIGN KEY (user_id) REFERENCES raffle.users (id);

ALTER TABLE raffle.raffles
    ADD CONSTRAINT fk_created_by_raffles FOREIGN KEY (created_by) REFERENCES raffle.users (id);


/*
 * Saul Echeverri
 * 11-06-2024
 * Se agrega la relación entre las tablas `raffles` y `tickets`.
 */
-- Agregar columna para la relación con la tabla Ticket
ALTER TABLE raffle.tickets
    ADD COLUMN raffle_id bigint NULL COMMENT 'Identificador del sorteo asociado al ticket';

-- Agregar restricción de clave externa para la nueva columna
ALTER TABLE raffle.tickets
    ADD CONSTRAINT fk_raffle_id_tickets FOREIGN KEY (raffle_id) REFERENCES raffle.raffles (id);


/*
 * Saul Echeverri
 * 11-06-2024
 * Se agrega la relación entre las tablas `customers` y `tickets`.
 */
-- Agregar restricción de clave externa para la nueva columna
ALTER TABLE raffle.tickets
    ADD CONSTRAINT fk_customer_id_tickets FOREIGN KEY (customer_id) REFERENCES raffle.customers (id);


/*
* Saul Echeverri
* 17-06-2024
* Se cambian los nombres de las restricciones de clave foránea en las tablas `tickets` y `raffles`.
*/
-- Eliminar las restricciones de clave foránea existentes
ALTER TABLE raffle.tickets DROP CONSTRAINT fk_user_id_tickets;
ALTER TABLE raffle.raffles DROP CONSTRAINT fk_created_by_raffles;
ALTER TABLE raffle.tickets DROP CONSTRAINT fk_raffle_id_tickets;
ALTER TABLE raffle.tickets DROP CONSTRAINT fk_customer_id_tickets;
ALTER TABLE raffle.roles_permissions DROP CONSTRAINT fk_role_id_roles_permissions;
ALTER TABLE raffle.roles_permissions DROP CONSTRAINT fk_permission_id_roles_permissions;
ALTER TABLE raffle.users_roles DROP CONSTRAINT fk_user_id_users_roles;
ALTER TABLE raffle.users_roles DROP CONSTRAINT fk_role_id_users_roles;

-- Agregar las restricciones de clave foránea con los nuevos nombres
ALTER TABLE raffle.tickets
    ADD CONSTRAINT fk_user_id_tickets FOREIGN KEY (user_id) REFERENCES raffle.users (id);

ALTER TABLE raffle.raffles
    ADD CONSTRAINT fk_created_by_raffles FOREIGN KEY (created_by) REFERENCES raffle.users (id);

ALTER TABLE raffle.tickets
    ADD CONSTRAINT fk_raffle_id_tickets FOREIGN KEY (raffle_id) REFERENCES raffle.raffles (id);

ALTER TABLE raffle.tickets
    ADD CONSTRAINT fk_customer_id_tickets FOREIGN KEY (customer_id) REFERENCES raffle.customers (id);

ALTER TABLE raffle.roles_permissions
    ADD CONSTRAINT fk_role_id_roles_permissions FOREIGN KEY (role_id) REFERENCES raffle.roles (id);

ALTER TABLE raffle.roles_permissions
    ADD CONSTRAINT fk_permission_id_roles_permissions FOREIGN KEY (permission_id) REFERENCES raffle.permissions (id);

ALTER TABLE raffle.users_roles
    ADD CONSTRAINT fk_user_id_users_roles FOREIGN KEY (user_id) REFERENCES raffle.users (id);

ALTER TABLE raffle.users_roles
    ADD CONSTRAINT fk_role_id_users_roles FOREIGN KEY (role_id) REFERENCES raffle.roles (id);


/*
* Saul Echeverri
* 17-06-2024
* Se agrega la columna raffle_selection a la tabla raffles
*/
ALTER TABLE raffle.raffles
    ADD COLUMN raffle_selection varchar(1) NULL;
COMMENT
ON COLUMN raffle.raffles.raffle_selection IS 'Indica si la rifa es por loteria o sorteo';
