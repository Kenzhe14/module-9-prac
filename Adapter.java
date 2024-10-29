interface IInternalDeliveryService {
    void deliverOrder(String orderId);
    void getDeliveryStatus(String orderId);
}

class InternalDeliveryService implements IInternalDeliveryService {
    @Override
    public void deliverOrder(String orderId) {
        System.out.println("Delivery " + orderId);
    }

    @Override
    public void getDeliveryStatus(String orderId) {
        System.out.println("Delivery Status = " + orderId);
    }
}

class ExternalLogisticsServiceA {
    void shipItem(int itemId) {
        System.out.println("Shipped = " + itemId);
    }

    void trackShipment(int shipmentId) {
        System.out.println("Track = " + shipmentId);
    }
}

class ExternalLogisticsServiceB {
    void sendPackage(String packageInfo) {
        System.out.println("Send = " + packageInfo);
    }

    void checkPackageStatus(String trackingCode) {
        System.out.println("Tracking = " + trackingCode);
    }
}

class LogisticsAdapterA implements IInternalDeliveryService {
    private ExternalLogisticsServiceA externalSystemA;

    public LogisticsAdapterA(ExternalLogisticsServiceA externalSystemA) {
        this.externalSystemA = externalSystemA;
    }

    @Override
    public void deliverOrder(String orderId) {
        int itemId = Integer.parseInt(orderId);
        externalSystemA.shipItem(itemId);
    }

    @Override
    public void getDeliveryStatus(String orderId) {
        int shipmentId = Integer.parseInt(orderId);
        externalSystemA.trackShipment(shipmentId);
    }
}

class LogisticsAdapterB implements IInternalDeliveryService {
    private ExternalLogisticsServiceB externalSystemB;

    public LogisticsAdapterB(ExternalLogisticsServiceB externalSystemB) {
        this.externalSystemB = externalSystemB;
    }

    @Override
    public void deliverOrder(String orderId) {
        externalSystemB.sendPackage(orderId);
    }

    @Override
    public void getDeliveryStatus(String orderId) {
        externalSystemB.checkPackageStatus(orderId);
    }
}

class DeliveryServiceFactory {
    public static IInternalDeliveryService getDeliveryService(String trackingCode) {
        if ("KZT".equals(trackingCode)) {
            return new InternalDeliveryService();
        } else if ("USD".equals(trackingCode)) {
            return new LogisticsAdapterA(new ExternalLogisticsServiceA());
        } else {
            return new LogisticsAdapterB(new ExternalLogisticsServiceB());
        }
    }
}

public class Adapter {
    public static void main(String[] args) {
        IInternalDeliveryService deliveryServiceKZT = DeliveryServiceFactory.getDeliveryService("KZT");
        deliveryServiceKZT.deliverOrder("12345");
        deliveryServiceKZT.getDeliveryStatus("12345");

        IInternalDeliveryService deliveryServiceUSD = DeliveryServiceFactory.getDeliveryService("USD");
        deliveryServiceUSD.deliverOrder("67890");
        deliveryServiceUSD.getDeliveryStatus("67890");

        IInternalDeliveryService deliveryServiceOther = DeliveryServiceFactory.getDeliveryService("EUR");
        deliveryServiceOther.deliverOrder("ABCDE");
        deliveryServiceOther.getDeliveryStatus("ABCDE");
    }
}
