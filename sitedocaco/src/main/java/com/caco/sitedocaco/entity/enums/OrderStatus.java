package com.caco.sitedocaco.entity.enums;

public enum OrderStatus {
    PENDING_PAYMENT,  // Aguardando pagamento (não contabilizado)
    PENDING,          // Pago, aguardando retirada
    DELIVERED         // Entregue/retirado
}
