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
import EntidadesOCR.Carro;
import EntidadesOCR.Linea;
import EntidadesOCR.LineaPK;
import EntidadesOCR.Renta;
import IntegracionDatos.exceptions.NonexistentEntityException;
import IntegracionDatos.exceptions.PreexistingEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Administrator
 */
public class LineaJpaController implements Serializable {

    public LineaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("OnlineCarRentalPU");

    public LineaJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Linea linea) throws PreexistingEntityException, Exception {
        if (linea.getLineaPK() == null) {
            linea.setLineaPK(new LineaPK());
        }
        linea.getLineaPK().setRentaid(linea.getRenta().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carro carroid = linea.getCarroid();
            if (carroid != null) {
                carroid = em.getReference(carroid.getClass(), carroid.getId());
                linea.setCarroid(carroid);
            }
            Renta renta = linea.getRenta();
            if (renta != null) {
                renta = em.getReference(renta.getClass(), renta.getId());
                linea.setRenta(renta);
            }
            em.persist(linea);
            if (carroid != null) {
                carroid.getLineaCollection().add(linea);
                carroid = em.merge(carroid);
            }
            if (renta != null) {
                renta.getLineaCollection().add(linea);
                renta = em.merge(renta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLinea(linea.getLineaPK()) != null) {
                throw new PreexistingEntityException("Linea " + linea + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Linea linea) throws NonexistentEntityException, Exception {
        linea.getLineaPK().setRentaid(linea.getRenta().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Linea persistentLinea = em.find(Linea.class, linea.getLineaPK());
            Carro carroidOld = persistentLinea.getCarroid();
            Carro carroidNew = linea.getCarroid();
            Renta rentaOld = persistentLinea.getRenta();
            Renta rentaNew = linea.getRenta();
            if (carroidNew != null) {
                carroidNew = em.getReference(carroidNew.getClass(), carroidNew.getId());
                linea.setCarroid(carroidNew);
            }
            if (rentaNew != null) {
                rentaNew = em.getReference(rentaNew.getClass(), rentaNew.getId());
                linea.setRenta(rentaNew);
            }
            linea = em.merge(linea);
            if (carroidOld != null && !carroidOld.equals(carroidNew)) {
                carroidOld.getLineaCollection().remove(linea);
                carroidOld = em.merge(carroidOld);
            }
            if (carroidNew != null && !carroidNew.equals(carroidOld)) {
                carroidNew.getLineaCollection().add(linea);
                carroidNew = em.merge(carroidNew);
            }
            if (rentaOld != null && !rentaOld.equals(rentaNew)) {
                rentaOld.getLineaCollection().remove(linea);
                rentaOld = em.merge(rentaOld);
            }
            if (rentaNew != null && !rentaNew.equals(rentaOld)) {
                rentaNew.getLineaCollection().add(linea);
                rentaNew = em.merge(rentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                LineaPK id = linea.getLineaPK();
                if (findLinea(id) == null) {
                    throw new NonexistentEntityException("The linea with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(LineaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Linea linea;
            try {
                linea = em.getReference(Linea.class, id);
                linea.getLineaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The linea with id " + id + " no longer exists.", enfe);
            }
            Carro carroid = linea.getCarroid();
            if (carroid != null) {
                carroid.getLineaCollection().remove(linea);
                carroid = em.merge(carroid);
            }
            Renta renta = linea.getRenta();
            if (renta != null) {
                renta.getLineaCollection().remove(linea);
                renta = em.merge(renta);
            }
            em.remove(linea);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Linea> findLineaEntities() {
        return findLineaEntities(true, -1, -1);
    }

    public List<Linea> findLineaEntities(int maxResults, int firstResult) {
        return findLineaEntities(false, maxResults, firstResult);
    }

    private List<Linea> findLineaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Linea.class));
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

    public Linea findLinea(LineaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Linea.class, id);
        } finally {
            em.close();
        }
    }

    public int getLineaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Linea> rt = cq.from(Linea.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void updateLine(Linea the_line, int num) {
        the_line.setCantidad(num);
        try {
            this.edit(the_line);
        } catch (Exception ex) {
            Logger.getLogger(LineaJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
