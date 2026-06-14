-- 1. Tabela de Usuários (tb_users)

CREATE TABLE tb_users (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL
);

-- 2. Tabela de Camisas (kits)

CREATE TABLE kits (
                      id BIGSERIAL PRIMARY KEY,
                      kit_year INTEGER NOT NULL,
                      name VARCHAR(100) NOT NULL,
                      image_url VARCHAR(255),
                      description TEXT
);

-- 3. Tabela de Rankings (rankings)

CREATE TABLE rankings (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          title VARCHAR(100) NOT NULL,
                          ranking_type VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_ranking_user
                              FOREIGN KEY (user_id)
                                  REFERENCES tb_users(id)
                                  ON DELETE CASCADE
);

-- 4. Tabela de Associação (kit_rankings)

CREATE TABLE kit_rankings (
                              ranking_id BIGINT NOT NULL,
                              kit_id BIGINT NOT NULL,
                              position INTEGER NOT NULL,

                              PRIMARY KEY (ranking_id, kit_id),

                              CONSTRAINT fk_kit_rankings_ranking
                                  FOREIGN KEY (ranking_id)
                                      REFERENCES rankings(id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_kit_rankings_kit
                                  FOREIGN KEY (kit_id)
                                      REFERENCES kits(id)
                                      ON DELETE CASCADE
);