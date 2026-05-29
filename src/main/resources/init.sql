-- ============================================================
-- TravelAgency — Script de datos iniciales y demo
-- Ejecutar en PostgreSQL: psql -U postgres -d travelagency
-- ============================================================

-- ============================================================
-- 1. STATUSES — Estados iniciales del sistema
-- ============================================================

INSERT INTO statuses (created_at, entity_type, name) VALUES
-- Usuario
(NOW(), 'USER', 'ACTIVE'),
(NOW(), 'USER', 'INACTIVE'),
-- Paquete
(NOW(), 'PACKAGE', 'AVAILABLE'),
(NOW(), 'PACKAGE', 'SOLD_OUT'),
(NOW(), 'PACKAGE', 'CANCELLED'),
-- Reserva
(NOW(), 'BOOKING', 'PENDING_PAYMENT'),
(NOW(), 'BOOKING', 'CONFIRMED'),
(NOW(), 'BOOKING', 'CANCELLED'),
(NOW(), 'BOOKING', 'EXPIRED'),
-- Pago
(NOW(), 'PAYMENT', 'APPROVED'),
-- Promoción
(NOW(), 'PROMOTION', 'ACTIVE'),
(NOW(), 'PROMOTION', 'INACTIVE'),
(NOW(), 'PROMOTION', 'EXPIRED'),
-- Categoría
(NOW(), 'CATEGORY', 'ACTIVE'),
(NOW(), 'CATEGORY', 'INACTIVE'),
-- Temporada
(NOW(), 'SEASON', 'ACTIVE'),
(NOW(), 'SEASON', 'INACTIVE'),
-- Destino
(NOW(), 'DESTINATION', 'ACTIVE'),
(NOW(), 'DESTINATION', 'INACTIVE'),
-- Tipo de paquete
(NOW(), 'PACKAGE_TYPE', 'ACTIVE'),
(NOW(), 'PACKAGE_TYPE', 'INACTIVE'),
-- Servicio
(NOW(), 'SERVICE', 'ACTIVE'),
(NOW(), 'SERVICE', 'INACTIVE');

-- ============================================================
-- 2. CATEGORÍAS
-- ============================================================

INSERT INTO categories (created_at, name, description, status_id) VALUES
(NOW(), 'ADVENTURE', 'Paquetes de aventura y actividades al aire libre', (SELECT id FROM statuses WHERE entity_type='CATEGORY' AND name='ACTIVE')),
(NOW(), 'CITY ESCAPE', 'Escapadas urbanas y turismo cultural', (SELECT id FROM statuses WHERE entity_type='CATEGORY' AND name='ACTIVE')),
(NOW(), 'FOOD', 'Turismo gastronómico y culinario', (SELECT id FROM statuses WHERE entity_type='CATEGORY' AND name='ACTIVE')),
(NOW(), 'RELAX', 'Paquetes de descanso y bienestar', (SELECT id FROM statuses WHERE entity_type='CATEGORY' AND name='ACTIVE'));

-- ============================================================
-- 3. TIPOS DE PAQUETE
-- ============================================================

INSERT INTO package_types (created_at, name, status_id) VALUES
(NOW(), 'NACIONAL', (SELECT id FROM statuses WHERE entity_type='PACKAGE_TYPE' AND name='ACTIVE')),
(NOW(), 'INTERNACIONAL', (SELECT id FROM statuses WHERE entity_type='PACKAGE_TYPE' AND name='ACTIVE'));

-- ============================================================
-- 4. TEMPORADAS
-- ============================================================

INSERT INTO seasons (created_at, name, status_id) VALUES
(NOW(), 'VERANO', (SELECT id FROM statuses WHERE entity_type='SEASON' AND name='ACTIVE')),
(NOW(), 'PRIMAVERA', (SELECT id FROM statuses WHERE entity_type='SEASON' AND name='ACTIVE')),
(NOW(), 'OTOÑO', (SELECT id FROM statuses WHERE entity_type='SEASON' AND name='ACTIVE')),
(NOW(), 'INVIERNO', (SELECT id FROM statuses WHERE entity_type='SEASON' AND name='ACTIVE'));

-- ============================================================
-- 5. DESTINOS
-- ============================================================

INSERT INTO destinations (created_at, name, description, status_id) VALUES
(NOW(), 'Ciudad de México, México', 'Capital cultural y gastronómica de México', (SELECT id FROM statuses WHERE entity_type='DESTINATION' AND name='ACTIVE')),
(NOW(), 'París, Francia', 'La ciudad de la luz y el amor', (SELECT id FROM statuses WHERE entity_type='DESTINATION' AND name='ACTIVE')),
(NOW(), 'San Pedro de Atacama, Chile', 'Desierto más árido del mundo con paisajes únicos', (SELECT id FROM statuses WHERE entity_type='DESTINATION' AND name='ACTIVE')),
(NOW(), 'Santiago, Chile', 'Capital de Chile con vida urbana y cultural', (SELECT id FROM statuses WHERE entity_type='DESTINATION' AND name='ACTIVE')),
(NOW(), 'Buenos Aires, Argentina', 'Capital del tango y la gastronomía rioplatense', (SELECT id FROM statuses WHERE entity_type='DESTINATION' AND name='ACTIVE')),
(NOW(), 'Cusco, Perú', 'Puerta de entrada a Machu Picchu y la cultura inca', (SELECT id FROM statuses WHERE entity_type='DESTINATION' AND name='ACTIVE'));

-- ============================================================
-- 6. SERVICIOS
-- ============================================================

INSERT INTO services (created_at, name, description, status_id) VALUES
(NOW(), 'Vuelo incluido', 'Vuelos de ida y vuelta incluidos en el paquete', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE')),
(NOW(), 'Hotel 4 estrellas', 'Alojamiento en hotel de 4 estrellas con desayuno', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE')),
(NOW(), 'Hotel 3 estrellas', 'Alojamiento en hotel de 3 estrellas con desayuno', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE')),
(NOW(), 'Traslados incluidos', 'Traslados aeropuerto-hotel-aeropuerto incluidos', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE')),
(NOW(), 'Guía turístico', 'Guía turístico local en español', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE')),
(NOW(), 'Seguro de viaje', 'Seguro de viaje con cobertura médica internacional', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE')),
(NOW(), 'Desayuno incluido', 'Desayuno buffet diario incluido', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE')),
(NOW(), 'Todas las comidas', 'Desayuno, almuerzo y cena incluidos', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE')),
(NOW(), 'Spa y bienestar', 'Acceso a spa, piscina y actividades de bienestar', (SELECT id FROM statuses WHERE entity_type='SERVICE' AND name='ACTIVE'));

-- ============================================================
-- 7. PAQUETES TURÍSTICOS DE DEMO
-- ============================================================

INSERT INTO tourist_packages (name, description, destination_id, start_date, end_date, duration_days, price, total_slots, available_slots, conditions, restrictions, package_type_id, category_id, season_id, status_id, created_at) VALUES

-- Paquete 1: París Internacional
('Escapada a París',
 'Descubre la ciudad del amor con visitas a la Torre Eiffel, el Louvre y Montmartre. Incluye vuelos ida y vuelta, hotel 4 estrellas y traslados.',
 (SELECT id FROM destinations WHERE name='París, Francia'),
 '2026-07-01', '2026-07-07', 6,
 1850000, 20, 20,
 'Pago total requerido antes de la fecha de salida. Documentación de viaje vigente obligatoria.',
 'Mayores de 18 años. Pasaporte con vigencia mínima de 6 meses.',
 (SELECT id FROM package_types WHERE name='INTERNACIONAL'),
 (SELECT id FROM categories WHERE name='CITY ESCAPE'),
 (SELECT id FROM seasons WHERE name='INVIERNO'),
 (SELECT id FROM statuses WHERE entity_type='PACKAGE' AND name='AVAILABLE'),
 NOW()),

-- Paquete 2: Atacama Aventura
('Aventura en el Desierto de Atacama',
 'Explora el desierto más árido del mundo. Geisers del Tatio al amanecer, Valle de la Luna, lagunas altiplánicas y observación de estrellas.',
 (SELECT id FROM destinations WHERE name='San Pedro de Atacama, Chile'),
 '2026-06-15', '2026-06-20', 5,
 650000, 15, 15,
 'Condición física moderada requerida. Ropa abrigada obligatoria para actividades nocturnas.',
 'No recomendado para menores de 12 años ni personas con problemas cardíacos.',
 (SELECT id FROM package_types WHERE name='NACIONAL'),
 (SELECT id FROM categories WHERE name='ADVENTURE'),
 (SELECT id FROM seasons WHERE name='INVIERNO'),
 (SELECT id FROM statuses WHERE entity_type='PACKAGE' AND name='AVAILABLE'),
 NOW()),

-- Paquete 3: Santiago City Break
('Santiago City Break',
 'Recorre lo mejor de Santiago en un fin de semana largo. Cerro San Cristóbal, Barrio Lastarria, Mercado Central y los mejores restaurantes de la ciudad.',
 (SELECT id FROM destinations WHERE name='Santiago, Chile'),
 '2026-06-01', '2026-06-04', 3,
 280000, 30, 30,
 'Incluye desayuno diario. Check-in disponible desde las 15:00 hrs.',
 NULL,
 (SELECT id FROM package_types WHERE name='NACIONAL'),
 (SELECT id FROM categories WHERE name='CITY ESCAPE'),
 (SELECT id FROM seasons WHERE name='INVIERNO'),
 (SELECT id FROM statuses WHERE entity_type='PACKAGE' AND name='AVAILABLE'),
 NOW()),

-- Paquete 4: México Gastronómico
('México: Sabores y Cultura',
 'Sumérgete en la gastronomía mexicana Patrimonio de la Humanidad. Visita Teotihuacán, Xochimilco y los mejores mercados artesanales de Ciudad de México.',
 (SELECT id FROM destinations WHERE name='Ciudad de México, México'),
 '2026-08-10', '2026-08-17', 7,
 1200000, 25, 25,
 'Pago del 50% al momento de reservar. Saldo restante 30 días antes de la salida.',
 'Vacuna contra fiebre amarilla recomendada. Documentación de viaje vigente.',
 (SELECT id FROM package_types WHERE name='INTERNACIONAL'),
 (SELECT id FROM categories WHERE name='FOOD'),
 (SELECT id FROM seasons WHERE name='VERANO'),
 (SELECT id FROM statuses WHERE entity_type='PACKAGE' AND name='AVAILABLE'),
 NOW()),

-- Paquete 5: Atacama Relax
('Relax y Bienestar en Atacama',
 'Estadía de bienestar en el corazón del desierto. Spa, yoga al amanecer, meditación y observación astronómica nocturna en uno de los cielos más limpios del mundo.',
 (SELECT id FROM destinations WHERE name='San Pedro de Atacama, Chile'),
 '2026-09-05', '2026-09-10', 5,
 890000, 10, 10,
 'Incluye todas las comidas. Actividades de bienestar incluidas en el precio.',
 'Mayores de 16 años. No apto para personas con movilidad reducida.',
 (SELECT id FROM package_types WHERE name='NACIONAL'),
 (SELECT id FROM categories WHERE name='RELAX'),
 (SELECT id FROM seasons WHERE name='PRIMAVERA'),
 (SELECT id FROM statuses WHERE entity_type='PACKAGE' AND name='AVAILABLE'),
 NOW()),

-- Paquete 6: Buenos Aires
('Buenos Aires: Tango y Gastronomía',
 'Descubre la Paris de Sudamérica. Shows de tango en La Boca, Recoleta, Puerto Madero y la mejor carne argentina en los parrillas más famosas de la ciudad.',
 (SELECT id FROM destinations WHERE name='Buenos Aires, Argentina'),
 '2026-07-15', '2026-07-21', 6,
 980000, 20, 20,
 'Vuelo y hotel incluidos. Traslados aeropuerto incluidos.',
 'Pasaporte vigente requerido para ciudadanos no chilenos.',
 (SELECT id FROM package_types WHERE name='INTERNACIONAL'),
 (SELECT id FROM categories WHERE name='FOOD'),
 (SELECT id FROM seasons WHERE name='INVIERNO'),
 (SELECT id FROM statuses WHERE entity_type='PACKAGE' AND name='AVAILABLE'),
 NOW()),

-- Paquete 7: Cusco y Machu Picchu
('Cusco y Machu Picchu: Imperio Inca',
 'Viaja al corazón del Imperio Inca. Explora Cusco, el Valle Sagrado de los Incas y la maravilla del mundo Machu Picchu con guía especializado.',
 (SELECT id FROM destinations WHERE name='Cusco, Perú'),
 '2026-08-20', '2026-08-27', 7,
 1450000, 18, 18,
 'Incluye tren a Machu Picchu y entrada al sitio arqueológico. Seguro de viaje obligatorio.',
 'Se recomienda aclimatación previa a la altura. No apto para personas con problemas cardíacos severos.',
 (SELECT id FROM package_types WHERE name='INTERNACIONAL'),
 (SELECT id FROM categories WHERE name='ADVENTURE'),
 (SELECT id FROM seasons WHERE name='VERANO'),
 (SELECT id FROM statuses WHERE entity_type='PACKAGE' AND name='AVAILABLE'),
 NOW());

-- ============================================================
-- 8. PROMOCIONES DE DEMO
-- ============================================================

INSERT INTO promotions (created_at, name, description, discount_percentage, min_passengers, min_bookings_session, min_bookings_history, is_accumulable, start_date, end_date, status_id) VALUES

-- Descuento por grupo
(NOW(), 'Descuento Grupo Familiar',
 'Descuento del 10% para grupos de 4 o más pasajeros',
 10.00, 4, NULL, NULL, true,
 NOW(), '2026-12-31 23:59:59',
 (SELECT id FROM statuses WHERE entity_type='PROMOTION' AND name='ACTIVE')),

-- Descuento cliente frecuente
(NOW(), 'Cliente Frecuente',
 'Descuento del 8% para clientes con 2 o más reservas confirmadas',
 8.00, NULL, NULL, 2, true,
 NOW(), '2026-12-31 23:59:59',
 (SELECT id FROM statuses WHERE entity_type='PROMOTION' AND name='ACTIVE')),

-- Descuento multi-paquete
(NOW(), 'Compra Múltiple',
 'Descuento del 5% al reservar 2 o más paquetes en la misma sesión',
 5.00, NULL, 2, NULL, true,
 NOW(), '2026-12-31 23:59:59',
 (SELECT id FROM statuses WHERE entity_type='PROMOTION' AND name='ACTIVE'));

-- ============================================================
-- Verificación final
-- ============================================================

SELECT 'Statuses' as tabla, COUNT(*) as registros FROM statuses
UNION ALL SELECT 'Categories', COUNT(*) FROM categories
UNION ALL SELECT 'Package Types', COUNT(*) FROM package_types
UNION ALL SELECT 'Seasons', COUNT(*) FROM seasons
UNION ALL SELECT 'Destinations', COUNT(*) FROM destinations
UNION ALL SELECT 'Services', COUNT(*) FROM services
UNION ALL SELECT 'Tourist Packages', COUNT(*) FROM tourist_packages
UNION ALL SELECT 'Promotions', COUNT(*) FROM promotions;

-- ============================================================
-- 9. ASOCIAR SERVICIOS A PAQUETES
-- ============================================================

-- Escapada a París: vuelo, hotel 4*, traslados, guía, seguro
INSERT INTO tourist_package_services (tourist_package_id, service_id) VALUES
((SELECT id FROM tourist_packages WHERE name='Escapada a París'), (SELECT id FROM services WHERE name='Vuelo incluido')),
((SELECT id FROM tourist_packages WHERE name='Escapada a París'), (SELECT id FROM services WHERE name='Hotel 4 estrellas')),
((SELECT id FROM tourist_packages WHERE name='Escapada a París'), (SELECT id FROM services WHERE name='Traslados incluidos')),
((SELECT id FROM tourist_packages WHERE name='Escapada a París'), (SELECT id FROM services WHERE name='Guía turístico')),
((SELECT id FROM tourist_packages WHERE name='Escapada a París'), (SELECT id FROM services WHERE name='Seguro de viaje'));

-- Aventura en Atacama: traslados, guía, desayuno, seguro
INSERT INTO tourist_package_services (tourist_package_id, service_id) VALUES
((SELECT id FROM tourist_packages WHERE name='Aventura en el Desierto de Atacama'), (SELECT id FROM services WHERE name='Traslados incluidos')),
((SELECT id FROM tourist_packages WHERE name='Aventura en el Desierto de Atacama'), (SELECT id FROM services WHERE name='Guía turístico')),
((SELECT id FROM tourist_packages WHERE name='Aventura en el Desierto de Atacama'), (SELECT id FROM services WHERE name='Desayuno incluido')),
((SELECT id FROM tourist_packages WHERE name='Aventura en el Desierto de Atacama'), (SELECT id FROM services WHERE name='Seguro de viaje'));

-- Santiago City Break: hotel 3*, desayuno, traslados
INSERT INTO tourist_package_services (tourist_package_id, service_id) VALUES
((SELECT id FROM tourist_packages WHERE name='Santiago City Break'), (SELECT id FROM services WHERE name='Hotel 3 estrellas')),
((SELECT id FROM tourist_packages WHERE name='Santiago City Break'), (SELECT id FROM services WHERE name='Desayuno incluido')),
((SELECT id FROM tourist_packages WHERE name='Santiago City Break'), (SELECT id FROM services WHERE name='Traslados incluidos'));

-- México Sabores y Cultura: vuelo, hotel 4*, traslados, guía, seguro
INSERT INTO tourist_package_services (tourist_package_id, service_id) VALUES
((SELECT id FROM tourist_packages WHERE name='México: Sabores y Cultura'), (SELECT id FROM services WHERE name='Vuelo incluido')),
((SELECT id FROM tourist_packages WHERE name='México: Sabores y Cultura'), (SELECT id FROM services WHERE name='Hotel 4 estrellas')),
((SELECT id FROM tourist_packages WHERE name='México: Sabores y Cultura'), (SELECT id FROM services WHERE name='Traslados incluidos')),
((SELECT id FROM tourist_packages WHERE name='México: Sabores y Cultura'), (SELECT id FROM services WHERE name='Guía turístico')),
((SELECT id FROM tourist_packages WHERE name='México: Sabores y Cultura'), (SELECT id FROM services WHERE name='Seguro de viaje'));

-- Relax en Atacama: todas las comidas, spa, traslados
INSERT INTO tourist_package_services (tourist_package_id, service_id) VALUES
((SELECT id FROM tourist_packages WHERE name='Relax y Bienestar en Atacama'), (SELECT id FROM services WHERE name='Todas las comidas')),
((SELECT id FROM tourist_packages WHERE name='Relax y Bienestar en Atacama'), (SELECT id FROM services WHERE name='Spa y bienestar')),
((SELECT id FROM tourist_packages WHERE name='Relax y Bienestar en Atacama'), (SELECT id FROM services WHERE name='Traslados incluidos')),
((SELECT id FROM tourist_packages WHERE name='Relax y Bienestar en Atacama'), (SELECT id FROM services WHERE name='Seguro de viaje'));

-- Buenos Aires: vuelo, hotel 4*, traslados, guía
INSERT INTO tourist_package_services (tourist_package_id, service_id) VALUES
((SELECT id FROM tourist_packages WHERE name='Buenos Aires: Tango y Gastronomía'), (SELECT id FROM services WHERE name='Vuelo incluido')),
((SELECT id FROM tourist_packages WHERE name='Buenos Aires: Tango y Gastronomía'), (SELECT id FROM services WHERE name='Hotel 4 estrellas')),
((SELECT id FROM tourist_packages WHERE name='Buenos Aires: Tango y Gastronomía'), (SELECT id FROM services WHERE name='Traslados incluidos')),
((SELECT id FROM tourist_packages WHERE name='Buenos Aires: Tango y Gastronomía'), (SELECT id FROM services WHERE name='Guía turístico')),
((SELECT id FROM tourist_packages WHERE name='Buenos Aires: Tango y Gastronomía'), (SELECT id FROM services WHERE name='Seguro de viaje'));

-- Cusco y Machu Picchu: vuelo, hotel 3*, traslados, guía, seguro
INSERT INTO tourist_package_services (tourist_package_id, service_id) VALUES
((SELECT id FROM tourist_packages WHERE name='Cusco y Machu Picchu: Imperio Inca'), (SELECT id FROM services WHERE name='Vuelo incluido')),
((SELECT id FROM tourist_packages WHERE name='Cusco y Machu Picchu: Imperio Inca'), (SELECT id FROM services WHERE name='Hotel 3 estrellas')),
((SELECT id FROM tourist_packages WHERE name='Cusco y Machu Picchu: Imperio Inca'), (SELECT id FROM services WHERE name='Traslados incluidos')),
((SELECT id FROM tourist_packages WHERE name='Cusco y Machu Picchu: Imperio Inca'), (SELECT id FROM services WHERE name='Guía turístico')),
((SELECT id FROM tourist_packages WHERE name='Cusco y Machu Picchu: Imperio Inca'), (SELECT id FROM services WHERE name='Seguro de viaje'));

-- ============================================================
-- 10. ASOCIAR PROMOCIONES A PAQUETES
-- ============================================================

-- Todas las promociones aplican a todos los paquetes
INSERT INTO tourist_package_promotions (tourist_package_id, promotion_id)
SELECT tp.id, p.id
FROM tourist_packages tp
CROSS JOIN promotions p;
