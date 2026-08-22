package com.ownProject.GINS.inventory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ownProject.GINS.jpa.InventoryRepository;
import com.ownProject.GINS.jpa.TransactionRepository;
import com.ownProject.GINS.notification.NotificationService;
import com.ownProject.GINS.product.Product;
import com.ownProject.GINS.wareHouse.WareHouse;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationService notificationService; // Inject new service mock

    @InjectMocks
    private InventoryService inventoryService;

    private UUID productId;
    private Integer warehouseId;
    private Product product;
    private WareHouse warehouse;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        warehouseId = 1;

        product = new Product();
        product.setId(productId);
        product.setName("Laptop");
        product.setLow_stock_threshold(5);

        warehouse = new WareHouse();
        warehouse.setId(warehouseId);
        warehouse.setName("Main Hub");

        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setWareHouse(warehouse);
        inventory.setQuantity(10);
    }

    // Test Case 1: Selling product successfully when stock is sufficient
    @Test
    void sellProduct_Success() {
        // Mock the pessimistic lock database query
        when(inventoryRepository.findByProduct_IdAndWareHouse_IdWithLock(productId, warehouseId)).thenReturn(inventory);
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inventory result = inventoryService.sellProduct(productId, warehouseId, 3);

        assertNotNull(result);
        assertEquals(7, result.getQuantity()); // 10 - 3 = 7
        verify(inventoryRepository, times(1)).save(inventory);
        verify(transactionRepository, times(1)).save(any());
        verifyNoInteractions(notificationService); // Should NOT trigger alerts since 7 > threshold (5)
    }

    // Test Case 2: Selling product throws exception when stock is insufficient
    @Test
    void sellProduct_InsufficientStock() {
        when(inventoryRepository.findByProduct_IdAndWareHouse_IdWithLock(productId, warehouseId)).thenReturn(inventory);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryService.sellProduct(productId, warehouseId, 15);
        });

        assertEquals("Insufficient stock! Only 10 available.", exception.getMessage());
        verify(inventoryRepository, never()).save(any());
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(notificationService);
    }

    // Test Case 3: Verify low stock notification trigger when quantity dips below threshold
    @Test
    void sellProduct_TriggersLowStockNotification() {
        when(inventoryRepository.findByProduct_IdAndWareHouse_IdWithLock(productId, warehouseId)).thenReturn(inventory);
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inventory result = inventoryService.sellProduct(productId, warehouseId, 6); // 10 - 6 = 4 (threshold is 5)

        assertEquals(4, result.getQuantity());
        // Verify that the notification service alert was triggered asynchronously
        verify(notificationService, times(1)).triggerLowStockAlert(any(Inventory.class));
    }
}
