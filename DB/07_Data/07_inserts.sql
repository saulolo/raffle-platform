

INSERT INTO raffle.permissions VALUES (1, 'CREATE');
INSERT INTO raffle.permissions VALUES (2, 'READ');
INSERT INTO raffle.permissions VALUES (3, 'UPDATE');
INSERT INTO raffle.permissions VALUES (4, 'DELETE');

INSERT INTO raffle.roles VALUES (1, 'ADMIN');
INSERT INTO raffle.roles VALUES (2, 'VOLUNTEER');

INSERT INTO raffle.roles_permissions VALUES (1, 1);
INSERT INTO raffle.roles_permissions VALUES (1, 2);
INSERT INTO raffle.roles_permissions VALUES (1, 3);
INSERT INTO raffle.roles_permissions VALUES (1, 4);
INSERT INTO raffle.roles_permissions VALUES (2, 2);
INSERT INTO raffle.roles_permissions VALUES (2, 3);

INSERT INTO raffle.users VALUES (1, '313313313', '70707087', 'Cedula', 'secheverri@example.com', 'Saul Echeverri', true, true, true, true, '123456789', 'saulolo');
INSERT INTO raffle.users VALUES (2, '312312312', '71071071', 'Cedula', 'jdiaz@example.com', 'Jaime Diaz', true, true, true, true, '987654321', 'jaime');
INSERT INTO raffle.users VALUES (3, '311311311', '72077652', 'Cedula', 'fvasquez@example.com', 'Felipe Vasquez', true, true, true, true, '1234', 'felipe');
INSERT INTO raffle.users VALUES (6, '314341314', '100974993', 'Cedula', 'yency@example.com', 'Yency Serrano', true, true, true, true, '4321', 'yency');

INSERT INTO raffle.users_roles VALUES (1, 1);
INSERT INTO raffle.users_roles VALUES (2, 1);
INSERT INTO raffle.users_roles VALUES (3, 2);
INSERT INTO raffle.users_roles VALUES (6, 1);


/*
 * Saul Echeverri
 * 26-05-2024
 * Sprint # 1
 * Descripción de la modificación:
 * - Se agregan registros de prueba a la tabla raffles.
 */
INSERT INTO raffle.raffles VALUES (NULL, '2024-05-26 17:09:29.773', 'Rifa para recaudar fondos 11', '', false, 'Rifa de ejemplo 11', 2, 'Viaje todo incluido 11', 600000, '2024-05-26 17:09:29.773', 'ACTIVE', 2000, NULL);


/*
* Saul Echeverri
* 27-06-2024
* Se agrega el permiso 'TICKET_SALES' a la tabla permissions
*/
INSERT INTO permissions (id, name)
VALUES (5, 'TICKET_SALES');


/*
* Saul Echeverri
* 27-06-2024
* Se agregan los registros role_id y permission_id a la tabla roles_permissions para aceeder al nuevo perimiso
* 'TICKET_SALES'
*/
INSERT INTO roles_permissions (role_id, permission_id)
VALUES (1, 5),
       (2, 5);





