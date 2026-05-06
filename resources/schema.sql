DROP TABLE IF EXISTS bids CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS auctions CASCADE;
DROP TABLE IF EXISTS physical_products CASCADE;
DROP TABLE IF EXISTS digital_products CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id VARCHAR(20),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    user_type VARCHAR(10) NOT NULL,
    balance DOUBLE PRECISION DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE categories (
    id VARCHAR(20),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE products (
    id VARCHAR(20),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price DOUBLE PRECISION,
    category_id VARCHAR(20),
    product_type VARCHAR(10) NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE physical_products (
    id VARCHAR(20),
    year INTEGER,
    weight DOUBLE PRECISION,
    CONSTRAINT pk_physical PRIMARY KEY (id),
    CONSTRAINT fk_physical_product FOREIGN KEY (id) REFERENCES products(id)  ON DELETE CASCADE
);

CREATE TABLE digital_products (
    id VARCHAR(20),
    format VARCHAR(10),
    license_key VARCHAR(100),
    CONSTRAINT pk_digital PRIMARY KEY (id),
    CONSTRAINT fk_digital_product FOREIGN KEY (id) REFERENCES products(id)  ON DELETE CASCADE
);

CREATE TABLE auctions (
    id VARCHAR(20),
    seller_id VARCHAR(20),
    product_id VARCHAR(20),
    starting_price DOUBLE PRECISION NOT NULL,
    current_price DOUBLE PRECISION NOT NULL,
    starting_time TIMESTAMP NOT NULL,
    ending_time TIMESTAMP NOT NULL,
    auction_status VARCHAR(20) NOT NULL,
    auction_type VARCHAR(20) NOT NULL,
    winner_id VARCHAR(20),
    CONSTRAINT pk_auctions PRIMARY KEY (id),
    CONSTRAINT fk_auction_seller FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_auction_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_auction_winner FOREIGN KEY (winner_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE bids (
    id VARCHAR(20),
    bidder_id VARCHAR(20),
    auction_id VARCHAR(20),
    amount DOUBLE PRECISION NOT NULL,
    placed_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT pk_bids PRIMARY KEY (id),
    CONSTRAINT fk_bid_bidder FOREIGN KEY (bidder_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_bid_auction FOREIGN KEY (auction_id) REFERENCES auctions(id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    id VARCHAR(20),
    user_id VARCHAR(20),
    message VARCHAR(500) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_notifications PRIMARY KEY (id),
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE transactions (
    id VARCHAR(20),
    auction_id VARCHAR(20),
    winner_id VARCHAR(20),
    seller_id VARCHAR(20),
    final_amount DOUBLE PRECISION NOT NULL,
    transaction_time TIMESTAMP NOT NULL,
    CONSTRAINT pk_transactions PRIMARY KEY (id),
    CONSTRAINT fk_transaction_auction FOREIGN KEY (auction_id) REFERENCES auctions(id) ON DELETE SET NULL,
    CONSTRAINT fk_transaction_winner FOREIGN KEY (winner_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_transaction_seller FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE SET NULL
);
