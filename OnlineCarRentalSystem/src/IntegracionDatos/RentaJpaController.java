/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegracionDatos;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import EntidadesOCR.Parametro;
import EntidadesOCR.Rentaxbillete;
import java.util.ArrayList;
import java.util.List;
import EntidadesOCR.Linea;
import EntidadesOCR.Renta;
import IntegracionDatos.exceptions.IllegalOrphanException;
import IntegracionDatos.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Administrator
 */
public class RentaJpaController implements Serializable {

    public RentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("OnlineCarRentalPU");

    public RentaJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Renta renta) {
        if (renta.getRentaxbilleteCollection() == null) {
            renta.setRentaxbilleteCollection(new ArrayList<Rentaxbillete>());
        }
        if (renta.getLineaCollection() == null) {
            renta.setLineaCollection(new ArrayList<Linea>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parametro parametroid = renta.getParametroid();
            if (parametroid != null) {
                parametroid = em.getReference(parametroid.getClass(), parametroid.getId());
                renta.setParametroid(parametroid);
            }
            List<Rentaxbillete> attachedRentaxbilleteCollection = new ArrayList<Rentaxbillete>();
            for (Rentaxbillete rentaxbilleteCollectionRentaxbilleteToAttach : renta.getRentaxbilleteCollection()) {
                rentaxbilleteCollectionRentaxbilleteToAttach = em.getReference(rentaxbilleteCollectionRentaxbilleteToAttach.getClass(), rentaxbilleteCollectionRentaxbilleteToAttach.getRentaxbilletePK());
                attachedRentaxbilleteCollection.add(rentaxbilleteCollectionRentaxbilleteToAttach);
            }
            renta.setRentaxbilleteCollection(attachedRentaxbilleteCollection);
            List<Linea> attachedLineaCollection = new ArrayList<Linea>();
            for (Linea lineaCollectionLineaToAttach : renta.getLineaCollection()) {
                lineaCollectionLineaToAttach = em.getReference(lineaCollectionLineaToAttach.getClass(), lineaCollectionLineaToAttach.getLineaPK());
                attachedLineaCollection.add(lineaCollectionLineaToAttach);
            }
            renta.setLineaCollection(attachedLineaCollection);
            em.persist(renta);
            if (parametroid != null) {
                parametroid.getRentaCollection().add(renta);
                parametroid = em.merge(parametroid);
            }
            for (Rentaxbillete rentaxbilleteCollectionRentaxbillete : renta.getRentaxbilleteCollection()) {
                Renta oldRentaOfRentaxbilleteCollectionRentaxbillete = rentaxbilleteCollectionRentaxbillete.getRenta();
                rentaxbilleteCollectionRentaxbillete.setRenta(renta);
                rentaxbilleteCollectionRentaxbillete = em.merge(rentaxbilleteCollectionRentaxbillete);
                if (oldRentaOfRentaxbilleteCollectionRentaxbillete != null) {
                    oldRentaOfRentaxbilleteCollectionRentaxbillete.getRentaxbilleteCollection().remove(rentaxbilleteCollectionRentaxbillete);
                    oldRentaOfRentaxbilleteCollectionRentaxbillete = em.merge(oldRentaOfRentaxbilleteCollectionRentaxbillete);
                }
            }
            for (Linea lineaCollectionLinea : renta.getLineaCollection()) {
                Renta oldRentaOfLineaCollectionLinea = lineaCollectionLinea.getRenta();
                lineaCollectionLinea.setRenta(renta);
                lineaCollectionLinea = em.merge(lineaCollectionLinea);
                if (oldRentaOfLineaCollectionLinea != null) {
                    oldRentaOfLineaCollectionLinea.getLineaCollection().remove(lineaCollectionLinea);
                    oldRentaOfLineaCollectionLinea = em.merge(oldRentaOfLineaCollectionLinea);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Renta renta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Renta persistentRenta = em.find(Renta.class, renta.getId());
            Parametro parametroidOld = persistentRenta.getParametroid();
            Parametro parametroidNew = renta.getParametroid();
            List<Rentaxbillete> rentaxbilleteCollectionOld = persistentRenta.getRentaxbilleteCollection();
            List<Rentaxbillete> rentaxbilleteCollectionNew = renta.getRentaxbilleteCollection();
            List<Linea> lineaCollectionOld = persistentRenta.getLineaCollection();
            List<Linea> lineaCollectionNew = renta.getLineaCollection();
            List<String> illegalOrphanMessages = null;
            for (Rentaxbillete rentaxbilleteCollectionOldRentaxbillete : rentaxbilleteCollectionOld) {
                if (!rentaxbilleteCollectionNew.contains(rentaxbilleteCollectionOldRentaxbillete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rentaxbillete " + rentaxbilleteCollectionOldRentaxbillete + " since its renta field is not nullable.");
                }
            }
            for (Linea lineaCollectionOldLinea : lineaCollectionOld) {
                if (!lineaCollectionNew.contains(lineaCollectionOldLinea)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Linea " + lineaCollectionOldLinea + " since its renta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (parametroidNew != null) {
                parametroidNew = em.getReference(parametroidNew.getClass(), parametroidNew.getId());
                renta.setParametroid(parametroidNew);
            }
            List<Rentaxbillete> attachedRentaxbilleteCollectionNew = new ArrayList<Rentaxbillete>();
            for (Rentaxbillete rentaxbilleteCollectionNewRentaxbilleteToAttach : rentaxbilleteCollectionNew) {
                rentaxbilleteCollectionNewRentaxbilleteToAttach = em.getReference(rentaxbilleteCollectionNewRentaxbilleteToAttach.getClass(), rentaxbilleteCollectionNewRentaxbilleteToAttach.getRentaxbilletePK());
                attachedRentaxbilleteCollectionNew.add(rentaxbilleteCollectionNewRentaxbilleteToAttach);
            }
            rentaxbilleteCollectionNew = attachedRentaxbilleteCollectionNew;
            renta.setRentaxbilleteCollection(rentaxbilleteCollectionNew);
            List<Linea> attachedLineaCollectionNew = new ArrayList<Linea>();
            for (Linea lineaCollectionNewLineaToAttach : lineaCollectionNew) {
                lineaCollectionNewLineaToAttach = em.getReference(lineaCollectionNewLineaToAttach.getClass(), lineaCollectionNewLineaToAttach.getLineaPK());
                attachedLineaCollectionNew.add(lineaCollectionNewLineaToAttach);
            }
            lineaCollectionNew = attachedLineaCollectionNew;
            renta.setLineaCollection(lineaCollectionNew);
            renta = em.merge(renta);
            if (parametroidOld != null && !parametroidOld.equals(parametroidNew)) {
                parametroidOld.getRentaCollection().remove(renta);
                parametroidOld = em.merge(parametroidOld);
            }
            if (parametroidNew != null && !parametroidNew.equals(parametroidOld)) {
                parametroidNew.getRentaCollection().add(renta);
                parametroidNew = em.merge(parametroidNew);
            }
            for (Rentaxbillete rentaxbilleteCollectionNewRentaxbillete : rentaxbilleteCollectionNew) {
                if (!rentaxbilleteCollectionOld.contains(rentaxbilleteCollectionNewRentaxbillete)) {
                    Renta oldRentaOfRentaxbilleteCollectionNewRentaxbillete = rentaxbilleteCollectionNewRentaxbillete.getRenta();
                    rentaxbilleteCollectionNewRentaxbillete.setRenta(renta);
                    rentaxbilleteCollectionNewRentaxbillete = em.merge(rentaxbilleteCollectionNewRentaxbillete);
                    if (oldRentaOfRentaxbilleteCollectionNewRentaxbillete != null && !oldRentaOfRentaxbilleteCollectionNewRentaxbillete.equals(renta)) {
                        oldRentaOfRentaxbilleteCollectionNewRentaxbillete.getRentaxbilleteCollection().remove(rentaxbilleteCollectionNewRentaxbillete);
                        oldRentaOfRentaxbilleteCollectionNewRentaxbillete = em.merge(oldRentaOfRentaxbilleteCollectionNewRentaxbillete);
                    }
                }
            }
            for (Linea lineaCollectionNewLinea : lineaCollectionNew) {
                if (!lineaCollectionOld.contains(lineaCollectionNewLinea)) {
                    Renta oldRentaOfLineaCollectionNewLinea = lineaCollectionNewLinea.getRenta();
                    lineaCollectionNewLinea.setRenta(renta);
                    lineaCollectionNewLinea = em.merge(lineaCollectionNewLinea);
                    if (oldRentaOfLineaCollectionNewLinea != null && !oldRentaOfLineaCollectionNewLinea.equals(renta)) {
                        oldRentaOfLineaCollectionNewLinea.getLineaCollection().remove(lineaCollectionNewLinea);
                        oldRentaOfLineaCollectionNewLinea = em.merge(oldRentaOfLineaCollectionNewLinea);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = renta.getId();
                if (findRenta(id) == null) {
                    throw new NonexistentEntityException("The renta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Renta renta;
            try {
                renta = em.getReference(Renta.class, id);
                renta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The renta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rentaxbillete> rentaxbilleteCollectionOrphanCheck = renta.getRentaxbilleteCollection();
            for (Rentaxbillete rentaxbilleteCollectionOrphanCheckRentaxbillete : rentaxbilleteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Renta (" + renta + ") cannot be destroyed since the Rentaxbillete " + rentaxbilleteCollectionOrphanCheckRentaxbillete + " in its rentaxbilleteCollection field has a non-nullable renta field.");
            }
            List<Linea> lineaCollectionOrphanCheck = renta.getLineaCollection();
            for (Linea lineaCollectionOrphanCheckLinea : lineaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Renta (" + renta + ") cannot be destroyed since the Linea " + lineaCollectionOrphanCheckLinea + " in its lineaCollection field has a non-nullable renta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Parametro parametroid = renta.getParametroid();
            if (parametroid != null) {
                parametroid.getRentaCollection().remove(renta);
                parametroid = em.merge(parametroid);
            }
            em.remove(renta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Renta> findRentaEntities() {
        return findRentaEntities(true, -1, -1);
    }

    public List<Renta> findRentaEntities(int maxResults, int firstResult) {
        return findRentaEntities(false, maxResults, firstResult);
    }

    private List<Renta> findRentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Renta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Renta findRenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Renta.class, id);
        } finally {
            em.close();
        }
    }

    public int getRentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Renta> rt = cq.from(Renta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList getLineaCollection(Integer id) {
        Query query=getEntityManager().createNativeQuery("SELECT L.* FROM LINEA L, RENTA R WHERE R.ID=? AND R.ID=L.RENTAID", Linea.class);
        query.setParameter(1, id);
        return (ArrayList) query.getResultList();
    }

}
