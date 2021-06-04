/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegracionDatos;

import EntidadesOCR.Carro;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import EntidadesOCR.Linea;
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
public class CarroJpaController implements Serializable {

    public CarroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("OnlineCarRentalPU");

    public CarroJpaController() {
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carro carro) {
        if (carro.getLineaCollection() == null) {
            carro.setLineaCollection(new ArrayList<Linea>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Linea> attachedLineaCollection = new ArrayList<Linea>();
            for (Linea lineaCollectionLineaToAttach : carro.getLineaCollection()) {
                lineaCollectionLineaToAttach = em.getReference(lineaCollectionLineaToAttach.getClass(), lineaCollectionLineaToAttach.getLineaPK());
                attachedLineaCollection.add(lineaCollectionLineaToAttach);
            }
            carro.setLineaCollection(attachedLineaCollection);
            em.persist(carro);
            for (Linea lineaCollectionLinea : carro.getLineaCollection()) {
                Carro oldCarroidOfLineaCollectionLinea = lineaCollectionLinea.getCarroid();
                lineaCollectionLinea.setCarroid(carro);
                lineaCollectionLinea = em.merge(lineaCollectionLinea);
                if (oldCarroidOfLineaCollectionLinea != null) {
                    oldCarroidOfLineaCollectionLinea.getLineaCollection().remove(lineaCollectionLinea);
                    oldCarroidOfLineaCollectionLinea = em.merge(oldCarroidOfLineaCollectionLinea);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carro carro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carro persistentCarro = em.find(Carro.class, carro.getId());
            Collection<Linea> lineaCollectionOld = persistentCarro.getLineaCollection();
            Collection<Linea> lineaCollectionNew = carro.getLineaCollection();
            List<String> illegalOrphanMessages = null;
            for (Linea lineaCollectionOldLinea : lineaCollectionOld) {
                if (!lineaCollectionNew.contains(lineaCollectionOldLinea)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Linea " + lineaCollectionOldLinea + " since its carroid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Linea> attachedLineaCollectionNew = new ArrayList<Linea>();
            for (Linea lineaCollectionNewLineaToAttach : lineaCollectionNew) {
                lineaCollectionNewLineaToAttach = em.getReference(lineaCollectionNewLineaToAttach.getClass(), lineaCollectionNewLineaToAttach.getLineaPK());
                attachedLineaCollectionNew.add(lineaCollectionNewLineaToAttach);
            }
            lineaCollectionNew = attachedLineaCollectionNew;
            carro.setLineaCollection(lineaCollectionNew);
            carro = em.merge(carro);
            for (Linea lineaCollectionNewLinea : lineaCollectionNew) {
                if (!lineaCollectionOld.contains(lineaCollectionNewLinea)) {
                    Carro oldCarroidOfLineaCollectionNewLinea = lineaCollectionNewLinea.getCarroid();
                    lineaCollectionNewLinea.setCarroid(carro);
                    lineaCollectionNewLinea = em.merge(lineaCollectionNewLinea);
                    if (oldCarroidOfLineaCollectionNewLinea != null && !oldCarroidOfLineaCollectionNewLinea.equals(carro)) {
                        oldCarroidOfLineaCollectionNewLinea.getLineaCollection().remove(lineaCollectionNewLinea);
                        oldCarroidOfLineaCollectionNewLinea = em.merge(oldCarroidOfLineaCollectionNewLinea);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carro.getId();
                if (findCarro(id) == null) {
                    throw new NonexistentEntityException("The carro with id " + id + " no longer exists.");
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
            Carro carro;
            try {
                carro = em.getReference(Carro.class, id);
                carro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Linea> lineaCollectionOrphanCheck = carro.getLineaCollection();
            for (Linea lineaCollectionOrphanCheckLinea : lineaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Carro (" + carro + ") cannot be destroyed since the Linea " + lineaCollectionOrphanCheckLinea + " in its lineaCollection field has a non-nullable carroid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(carro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carro> findCarroEntities() {
        return findCarroEntities(true, -1, -1);
    }

    public List<Carro> findCarroEntities(int maxResults, int firstResult) {
        return findCarroEntities(false, maxResults, firstResult);
    }

    public List<Carro> findCarroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carro.class));
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

    public Carro findCarro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carro.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carro> rt = cq.from(Carro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public boolean carExists(Integer id) {
        return getEntityManager().find(Carro.class, id) != null;
    }

    public boolean carAvailable(Integer id, Integer cantidad) {
        return getEntityManager().find(Carro.class, id).getUnidadesdisponibles() >= cantidad;
    }

    public Integer consultarPrecioCarro(Integer id) {
        Integer precio=getEntityManager().find(Carro.class, id).getPrecio();
        return precio;
    }
}
