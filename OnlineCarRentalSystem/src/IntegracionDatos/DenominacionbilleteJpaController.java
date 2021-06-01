/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegracionDatos;

import EntidadesOCR.Denominacionbillete;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import EntidadesOCR.Rentaxbillete;
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
public class DenominacionbilleteJpaController implements Serializable {

    public DenominacionbilleteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("OnlineCarRentalPU");

    public DenominacionbilleteJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Denominacionbillete denominacionbillete) {
        if (denominacionbillete.getRentaxbilleteCollection() == null) {
            denominacionbillete.setRentaxbilleteCollection(new ArrayList<Rentaxbillete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Rentaxbillete> attachedRentaxbilleteCollection = new ArrayList<Rentaxbillete>();
            for (Rentaxbillete rentaxbilleteCollectionRentaxbilleteToAttach : denominacionbillete.getRentaxbilleteCollection()) {
                rentaxbilleteCollectionRentaxbilleteToAttach = em.getReference(rentaxbilleteCollectionRentaxbilleteToAttach.getClass(), rentaxbilleteCollectionRentaxbilleteToAttach.getRentaxbilletePK());
                attachedRentaxbilleteCollection.add(rentaxbilleteCollectionRentaxbilleteToAttach);
            }
            denominacionbillete.setRentaxbilleteCollection(attachedRentaxbilleteCollection);
            em.persist(denominacionbillete);
            for (Rentaxbillete rentaxbilleteCollectionRentaxbillete : denominacionbillete.getRentaxbilleteCollection()) {
                Denominacionbillete oldDenominacionbilleteOfRentaxbilleteCollectionRentaxbillete = rentaxbilleteCollectionRentaxbillete.getDenominacionbillete();
                rentaxbilleteCollectionRentaxbillete.setDenominacionbillete(denominacionbillete);
                rentaxbilleteCollectionRentaxbillete = em.merge(rentaxbilleteCollectionRentaxbillete);
                if (oldDenominacionbilleteOfRentaxbilleteCollectionRentaxbillete != null) {
                    oldDenominacionbilleteOfRentaxbilleteCollectionRentaxbillete.getRentaxbilleteCollection().remove(rentaxbilleteCollectionRentaxbillete);
                    oldDenominacionbilleteOfRentaxbilleteCollectionRentaxbillete = em.merge(oldDenominacionbilleteOfRentaxbilleteCollectionRentaxbillete);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Denominacionbillete denominacionbillete) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Denominacionbillete persistentDenominacionbillete = em.find(Denominacionbillete.class, denominacionbillete.getId());
            Collection<Rentaxbillete> rentaxbilleteCollectionOld = persistentDenominacionbillete.getRentaxbilleteCollection();
            Collection<Rentaxbillete> rentaxbilleteCollectionNew = denominacionbillete.getRentaxbilleteCollection();
            List<String> illegalOrphanMessages = null;
            for (Rentaxbillete rentaxbilleteCollectionOldRentaxbillete : rentaxbilleteCollectionOld) {
                if (!rentaxbilleteCollectionNew.contains(rentaxbilleteCollectionOldRentaxbillete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rentaxbillete " + rentaxbilleteCollectionOldRentaxbillete + " since its denominacionbillete field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Rentaxbillete> attachedRentaxbilleteCollectionNew = new ArrayList<Rentaxbillete>();
            for (Rentaxbillete rentaxbilleteCollectionNewRentaxbilleteToAttach : rentaxbilleteCollectionNew) {
                rentaxbilleteCollectionNewRentaxbilleteToAttach = em.getReference(rentaxbilleteCollectionNewRentaxbilleteToAttach.getClass(), rentaxbilleteCollectionNewRentaxbilleteToAttach.getRentaxbilletePK());
                attachedRentaxbilleteCollectionNew.add(rentaxbilleteCollectionNewRentaxbilleteToAttach);
            }
            rentaxbilleteCollectionNew = attachedRentaxbilleteCollectionNew;
            denominacionbillete.setRentaxbilleteCollection(rentaxbilleteCollectionNew);
            denominacionbillete = em.merge(denominacionbillete);
            for (Rentaxbillete rentaxbilleteCollectionNewRentaxbillete : rentaxbilleteCollectionNew) {
                if (!rentaxbilleteCollectionOld.contains(rentaxbilleteCollectionNewRentaxbillete)) {
                    Denominacionbillete oldDenominacionbilleteOfRentaxbilleteCollectionNewRentaxbillete = rentaxbilleteCollectionNewRentaxbillete.getDenominacionbillete();
                    rentaxbilleteCollectionNewRentaxbillete.setDenominacionbillete(denominacionbillete);
                    rentaxbilleteCollectionNewRentaxbillete = em.merge(rentaxbilleteCollectionNewRentaxbillete);
                    if (oldDenominacionbilleteOfRentaxbilleteCollectionNewRentaxbillete != null && !oldDenominacionbilleteOfRentaxbilleteCollectionNewRentaxbillete.equals(denominacionbillete)) {
                        oldDenominacionbilleteOfRentaxbilleteCollectionNewRentaxbillete.getRentaxbilleteCollection().remove(rentaxbilleteCollectionNewRentaxbillete);
                        oldDenominacionbilleteOfRentaxbilleteCollectionNewRentaxbillete = em.merge(oldDenominacionbilleteOfRentaxbilleteCollectionNewRentaxbillete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = denominacionbillete.getId();
                if (findDenominacionbillete(id) == null) {
                    throw new NonexistentEntityException("The denominacionbillete with id " + id + " no longer exists.");
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
            Denominacionbillete denominacionbillete;
            try {
                denominacionbillete = em.getReference(Denominacionbillete.class, id);
                denominacionbillete.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The denominacionbillete with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Rentaxbillete> rentaxbilleteCollectionOrphanCheck = denominacionbillete.getRentaxbilleteCollection();
            for (Rentaxbillete rentaxbilleteCollectionOrphanCheckRentaxbillete : rentaxbilleteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Denominacionbillete (" + denominacionbillete + ") cannot be destroyed since the Rentaxbillete " + rentaxbilleteCollectionOrphanCheckRentaxbillete + " in its rentaxbilleteCollection field has a non-nullable denominacionbillete field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(denominacionbillete);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Denominacionbillete> findDenominacionbilleteEntities() {
        return findDenominacionbilleteEntities(true, -1, -1);
    }

    public List<Denominacionbillete> findDenominacionbilleteEntities(int maxResults, int firstResult) {
        return findDenominacionbilleteEntities(false, maxResults, firstResult);
    }

    private List<Denominacionbillete> findDenominacionbilleteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Denominacionbillete.class));
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

    public Denominacionbillete findDenominacionbillete(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Denominacionbillete.class, id);
        } finally {
            em.close();
        }
    }

    public int getDenominacionbilleteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Denominacionbillete> rt = cq.from(Denominacionbillete.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
