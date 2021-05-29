/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegracionDatos;

import IntegracionDatos.exceptions.NonexistentEntityException;
import IntegracionDatos.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import EntidadesOCR.Denominacionbillete;
import EntidadesOCR.Renta;
import EntidadesOCR.Rentaxbillete;
import EntidadesOCR.RentaxbilletePK;

/**
 *
 * @author Administrator
 */
public class RentaxbilleteJpaController implements Serializable {

    public RentaxbilleteJpaController() {
    }

    public RentaxbilleteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("OnlineCarRentalPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rentaxbillete rentaxbillete) throws PreexistingEntityException, Exception {
        if (rentaxbillete.getRentaxbilletePK() == null) {
            rentaxbillete.setRentaxbilletePK(new RentaxbilletePK());
        }
        rentaxbillete.getRentaxbilletePK().setDenominacionbilleteid(rentaxbillete.getDenominacionbillete().getId());
        rentaxbillete.getRentaxbilletePK().setRentaid(rentaxbillete.getRenta().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Denominacionbillete denominacionbillete = rentaxbillete.getDenominacionbillete();
            if (denominacionbillete != null) {
                denominacionbillete = em.getReference(denominacionbillete.getClass(), denominacionbillete.getId());
                rentaxbillete.setDenominacionbillete(denominacionbillete);
            }
            Renta renta = rentaxbillete.getRenta();
            if (renta != null) {
                renta = em.getReference(renta.getClass(), renta.getId());
                rentaxbillete.setRenta(renta);
            }
            em.persist(rentaxbillete);
            if (denominacionbillete != null) {
                denominacionbillete.getRentaxbilleteCollection().add(rentaxbillete);
                denominacionbillete = em.merge(denominacionbillete);
            }
            if (renta != null) {
                renta.getRentaxbilleteCollection().add(rentaxbillete);
                renta = em.merge(renta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRentaxbillete(rentaxbillete.getRentaxbilletePK()) != null) {
                throw new PreexistingEntityException("Rentaxbillete " + rentaxbillete + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rentaxbillete rentaxbillete) throws NonexistentEntityException, Exception {
        rentaxbillete.getRentaxbilletePK().setDenominacionbilleteid(rentaxbillete.getDenominacionbillete().getId());
        rentaxbillete.getRentaxbilletePK().setRentaid(rentaxbillete.getRenta().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rentaxbillete persistentRentaxbillete = em.find(Rentaxbillete.class, rentaxbillete.getRentaxbilletePK());
            Denominacionbillete denominacionbilleteOld = persistentRentaxbillete.getDenominacionbillete();
            Denominacionbillete denominacionbilleteNew = rentaxbillete.getDenominacionbillete();
            Renta rentaOld = persistentRentaxbillete.getRenta();
            Renta rentaNew = rentaxbillete.getRenta();
            if (denominacionbilleteNew != null) {
                denominacionbilleteNew = em.getReference(denominacionbilleteNew.getClass(), denominacionbilleteNew.getId());
                rentaxbillete.setDenominacionbillete(denominacionbilleteNew);
            }
            if (rentaNew != null) {
                rentaNew = em.getReference(rentaNew.getClass(), rentaNew.getId());
                rentaxbillete.setRenta(rentaNew);
            }
            rentaxbillete = em.merge(rentaxbillete);
            if (denominacionbilleteOld != null && !denominacionbilleteOld.equals(denominacionbilleteNew)) {
                denominacionbilleteOld.getRentaxbilleteCollection().remove(rentaxbillete);
                denominacionbilleteOld = em.merge(denominacionbilleteOld);
            }
            if (denominacionbilleteNew != null && !denominacionbilleteNew.equals(denominacionbilleteOld)) {
                denominacionbilleteNew.getRentaxbilleteCollection().add(rentaxbillete);
                denominacionbilleteNew = em.merge(denominacionbilleteNew);
            }
            if (rentaOld != null && !rentaOld.equals(rentaNew)) {
                rentaOld.getRentaxbilleteCollection().remove(rentaxbillete);
                rentaOld = em.merge(rentaOld);
            }
            if (rentaNew != null && !rentaNew.equals(rentaOld)) {
                rentaNew.getRentaxbilleteCollection().add(rentaxbillete);
                rentaNew = em.merge(rentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                RentaxbilletePK id = rentaxbillete.getRentaxbilletePK();
                if (findRentaxbillete(id) == null) {
                    throw new NonexistentEntityException("The rentaxbillete with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(RentaxbilletePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rentaxbillete rentaxbillete;
            try {
                rentaxbillete = em.getReference(Rentaxbillete.class, id);
                rentaxbillete.getRentaxbilletePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rentaxbillete with id " + id + " no longer exists.", enfe);
            }
            Denominacionbillete denominacionbillete = rentaxbillete.getDenominacionbillete();
            if (denominacionbillete != null) {
                denominacionbillete.getRentaxbilleteCollection().remove(rentaxbillete);
                denominacionbillete = em.merge(denominacionbillete);
            }
            Renta renta = rentaxbillete.getRenta();
            if (renta != null) {
                renta.getRentaxbilleteCollection().remove(rentaxbillete);
                renta = em.merge(renta);
            }
            em.remove(rentaxbillete);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rentaxbillete> findRentaxbilleteEntities() {
        return findRentaxbilleteEntities(true, -1, -1);
    }

    public List<Rentaxbillete> findRentaxbilleteEntities(int maxResults, int firstResult) {
        return findRentaxbilleteEntities(false, maxResults, firstResult);
    }

    private List<Rentaxbillete> findRentaxbilleteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rentaxbillete.class));
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

    public Rentaxbillete findRentaxbillete(RentaxbilletePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rentaxbillete.class, id);
        } finally {
            em.close();
        }
    }

    public int getRentaxbilleteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rentaxbillete> rt = cq.from(Rentaxbillete.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
