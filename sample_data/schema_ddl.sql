DROP TABLE IF EXISTS public.cell;
DROP TABLE IF EXISTS public.map;
DROP TABLE IF EXISTS public.enemy;
DROP TABLE IF EXISTS public.player_item;
DROP TABLE IF EXISTS public.item;
DROP TABLE IF EXISTS public.player;
DROP TABLE IF EXISTS public.game_state;



CREATE TABLE public.game_state (
    id integer UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE public.map (
    id integer UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    width integer NOT NULL,
    height integer NOT NULL,
    state_id integer NOT NULL
);

CREATE TABLE public.cell(
    id integer UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    type varchar NOT NULL,
    x integer NOT NULL,
    y integer NOT NULL,
    map_id integer NOT NULL,
    enemy_id integer,
    item_id integer
);

CREATE TABLE public.enemy(
    id integer UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    hp integer NOT NULL,
    type varchar NOT NULL
);

CREATE TABLE public.item(
    id integer UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name varchar NOT NULL
);

CREATE TABLE public.player (
    id integer UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name text NOT NULL,
    hp integer NOT NULL,
    x integer NOT NULL,
    y integer NOT NULL,
    state_id integer NOT NULL
);

CREATE TABLE public.player_item (
    id integer UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    player_id integer NOT NULL,
    item_id integer NOT NULL
);

ALTER TABLE ONLY public.map
    ADD CONSTRAINT fk_state_id FOREIGN KEY (state_id) REFERENCES public.game_state(id);

ALTER TABLE ONLY public.cell
    ADD CONSTRAINT fk_map_id FOREIGN KEY (map_id) REFERENCES public.map(id),
    ADD CONSTRAINT fk_enemy_id FOREIGN KEY (enemy_id) REFERENCES public.enemy(id),
    ADD CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES public.item(id);

ALTER TABLE ONLY public.player
    ADD CONSTRAINT fk_state_id FOREIGN KEY (state_id) REFERENCES public.game_state(id);

ALTER TABLE ONLY public.player_item
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id),
    ADD CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES public.item(id);

INSERT INTO item (name)
VALUES ('sword'),
       ('key');