-- Create Customer table
CREATE TABLE Customer (
                          id UUID PRIMARY KEY,
                          login VARCHAR(255) UNIQUE,
                          email VARCHAR(255) UNIQUE,
                          first_name VARCHAR(255),
                          surname VARCHAR(255)
);

-- Create Transaction table
CREATE TABLE Transaction (
                             id UUID PRIMARY KEY,
                             amount DECIMAL(19,2),
                             create_date TIMESTAMP,
                             last_update_date TIMESTAMP,
                             reward_points BIGINT,
                             customer_id UUID,
                             FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

-- Add transactions column to Customer table
ALTER TABLE Customer
    ADD COLUMN transactions UUID ARRAY;

-- Create index for transactions column