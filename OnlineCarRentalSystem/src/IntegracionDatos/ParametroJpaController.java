/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegracionDatos;

import EntidadesOCR.Parametro;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import EntidadesOCR.Renta;
import IntegracionDatos.exceptions.IllegalOrphanException;
import IntegracionDatos.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Administrator
 */
public class ParametroJpaController implements Serializable {

    public ParametroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("OnlineCarRentalPU");

    public ParametroJpaController() {
       }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Parametro parametro) {
        if (parametro.getRentaCollection() == null) {
            parametro.setRentaCollection(new ArrayList<Renta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Renta> attachedRentaCollection = new ArrayList<Renta>();
            for (Renta rentaCollectionRentaToAttach : parametro.getRentaCollection()) {
                rentaCollectionRentaToAttach = em.getReference(rentaCollectionRentaToAttach.getClass(), rentaCollectionRentaToAttach.getId());
                attachedRentaCollection.add(rentaCollectionRentaToAttach);
            }
            parametro.setRentaCollection(attachedRentaCollection);
            em.persist(parametro);
            for (Renta rentaCollectionRenta : parametro.getRentaCollection()) {
                Parametro oldParametroidOfRentaCollectionRenta = rentaCollectionRenta.getParametroid();
                rentaCollectionRenta.setParametroid(parametro);
                rentaCollectionRenta = em.merge(rentaCollectionRenta);
                if (oldParametroidOfRentaCollectionRenta != null) {
                    oldParametroidOfRentaCollectionRenta.getRentaCollection().remove(rentaCollectionRenta);
                    oldParametroidOfRentaCollectionRenta = em.merge(oldParametroidOfRentaCollectionRenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Parametro parametro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Parametro persistentParametro = em.find(Parametro.class, parametro.getId());
            Collection<Renta> rentaCollectionOld = persistentParametro.getRentaCollection();
            Collection<Renta> rentaCollectionNew = parametro.getRentaCollection();
            List<String> illegalOrphanMessages = null;
            for (Renta rentaCollectionOldRenta : rentaCollectionOld) {
                if (!rentaCollectionNew.contains(rentaCollectionOldRenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Renta " + rentaCollectionOldRenta + " since its parametroid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Renta> attachedRentaCollectionNew = new ArrayList<Renta>();
            for (Renta rentaCollectionNewRentaToAttach : rentaCollectionNew) {
                rentaCollectionNewRentaToAttach = em.getReference(rentaCollectionNewRentaToAttach.getClass(), rentaCollectionNewRentaToAttach.getId());
                attachedRentaCollectionNew.add(rentaCollectionNewRentaToAttach);
            }
            rentaCollectionNew = attachedRentaCollectionNew;
            parametro.setRentaCollection(rentaCollectionNew);
            parametro = em.merge(parametro);
            for (Renta rentaCollectionNewRenta : rentaCollectionNew) {
                if (!rentaCollectionOld.contains(rentaCollectionNewRenta)) {
                    Parametro oldParametroidOfRentaCollectionNewRenta = rentaCollectionNewRenta.getParametroid();
                    rentaCollectionNewRenta.setParametroid(parametro);
                    rentaCollectionNewRenta = em.merge(rentaCollectionNewRenta);
                    if (oldParametroidOfRentaCollectionNewRenta != null && !oldParametroidOfRentaCollectionNewRenta.equals(parametro)) {
                        oldParametroidOfRentaCollectionNewRenta.getRentaCollection().remove(rentaCollectionNewRenta);
                        oldParametroidOfRentaCollectionNewRenta = em.merge(oldParametroidOfRentaCollectionNewRenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = parametro.getId();
                if (findParametro(id) == null) {
                    throw new NonexistentEntityException("The parametro with id " + id + " no longer exists.");
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
            Parametro parametro;
            try {
                parametro = em.getReference(Parametro.class, id);
                parametro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parametro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Renta> rentaCollectionOrphanCheck = parametro.getRentaCollection();
            for (Renta rentaCollectionOrphanCheckRenta : rentaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Parametro (" + parametro + ") cannot be destroyed since the Renta " + rentaCollectionOrphanCheckRenta + " in its rentaCollection field has a non-nullable parametroid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(parametro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Parametro> findParametroEntities() {
        return findParametroEntities(true, -1, -1);
    }

    public List<Parametro> findParametroEntities(int maxResults, int firstResult) {
        return findParametroEntities(false, maxResults, firstResult);
    }

    private List<Parametro> findParametroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Parametro.class));
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

    public Parametro findParametro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Parametro.class, id);
        } finally {
            em.close();
        }
    }

    public int getParametroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Parametro> rt = cq.from(Parametro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
