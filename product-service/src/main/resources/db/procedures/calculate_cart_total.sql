CREATE OR REPLACE PROCEDURE calculate_cart_total(
    p_user_id IN NUMBER,
    p_subtotal OUT NUMBER,
    p_tax OUT NUMBER,
    p_discount OUT NUMBER,
    p_total OUT NUMBER
) AS
BEGIN
    -- Calculate subtotal from cart items
    SELECT NVL(SUM(ci.quantity * ci.price), 0)
    INTO p_subtotal
    FROM CART_ITEMS ci
    WHERE ci.user_id = p_user_id;
    
    -- Calculate tax (10%)
    p_tax := p_subtotal * 0.10;
    
    -- Apply discount based on subtotal
    IF p_subtotal > 1000 THEN
        p_discount := p_subtotal * 0.15; -- 15% for orders over 1000
    ELSIF p_subtotal > 500 THEN
        p_discount := p_subtotal * 0.10; -- 10% for orders over 500
    ELSIF p_subtotal > 200 THEN
        p_discount := p_subtotal * 0.05; -- 5% for orders over 200
    ELSE
        p_discount := 0;
    END IF;
    
    -- Calculate final total
    p_total := p_subtotal + p_tax - p_discount;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_subtotal := 0;
        p_tax := 0;
        p_discount := 0;
        p_total := 0;
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Error calculating cart total: ' || SQLERRM);
END calculate_cart_total;
/