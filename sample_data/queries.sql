-- Get cells by state_id
SELECT cell.id, type, x, y, enemy_id, item_id, saved_at FROM cell
LEFT JOIN map m ON cell.map_id = m.id
LEFT JOIN game_state s ON m.state_id = s.id
WHERE state_id = 1
ORDER BY x, y;

-- GET EVERYTHING BY STATE
SELECT cell.id, cell.type celltype, x, y, e.type enemytype, e.hp enemyhp, item_id, i.name itemname, saved_at
FROM cell
         LEFT JOIN map m ON cell.map_id = m.id
         LEFT JOIN game_state s ON m.state_id = s.id
         LEFT JOIN item i ON i.id = cell.item_id
         LEFT JOIN enemy e ON cell.enemy_id = e.id
WHERE state_id = 1
ORDER BY x, y;

-- Get enemy info by id
SELECT *
FROM enemy
WHERE id = 1;

-- Get item info by id
SELECT *
FROM item
WHERE id = 1;

-- Get items for player_id
SELECT item.id, item.name
FROM item
LEFT JOIN player_item pi ON item.id = pi.item_id
LEFT JOIN player p ON p.id = pi.player_id
WHERE player_id = 4;

-- Get all players
SELECT id, name, hp, x, y, state_id
FROM player
