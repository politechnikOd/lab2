package Util;

import Model.SupplierEntity;
import Model.WorkerEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class GetListForChoiceBox {

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");

    public static List<String> getMaterialsList() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        Query q = em.createQuery("SELECT m.name as materialName, m.quantity as materialQuantity, c.name as categoryname FROM MaterialEntity m " +
                "INNER JOIN CategoryEntity c on m.categoryByIdCategory.idCategory = c.idCategory");
        List<Object[]> categories = q.getResultList();
        ArrayList<Object[]> categoryObjectList = new ArrayList<>();
        categoryObjectList.addAll(categories);
        List<String> materialInfo = new ArrayList<>();
        for (Object[] c : categoryObjectList) {
            materialInfo.add(c[0] + " " + c[1] + " " + c[2]);
        }
        em.close();
        return materialInfo;
    }

    public static List<String> getSuppliersList() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        TypedQuery<SupplierEntity> tq = em.createQuery("SELECT s FROM SupplierEntity s", SupplierEntity.class);
        List<SupplierEntity> suppliers = tq.getResultList();
        List<String> supplierNames = new ArrayList<>();
        for (SupplierEntity s : suppliers) {
            supplierNames.add(s.getName());
        }
        em.close();
        return supplierNames;
    }
}
