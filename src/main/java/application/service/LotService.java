package application.service;

import org.springframework.stereotype.Service;
import application.models.Lot;
import application.models.Searching;

import java.time.LocalDateTime;
import java.util.*;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import application.repository.LotRepo;

@Service

public class LotService {
    private final LotRepo lotRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public LotService(LotRepo lotRepo) {
        this.lotRepo = lotRepo;
    }

    public List<Lot> list() {
        return this.lotRepo.list();
    }

 

    public List<Lot> search(Searching s) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lot> query = builder.createQuery(Lot.class);
        Root<Lot> lot = query.from(Lot.class);
        Predicate criteria = builder.conjunction();
        if (s.getMotCle() != null || s.getMotCle().compareTo("") != 0) {
            Predicate nameLike = builder.like(
                    builder.lower(lot.get("nom")),
                    "%" + s.getMotCle().toLowerCase() + "%");
            criteria = builder.and(criteria, nameLike);
        }
        if (s.getDate() != null) {
            Predicate date = builder.lessThanOrEqualTo(lot.get("dateDebut"), s.getDate());
            criteria = builder.and(criteria, date);
        }
        if (s.getId_categorie() != 0) {
            Predicate categorie = builder.equal(lot.get("categorie").get("id_categorie"), s.getId_categorie());
            criteria = builder.and(criteria, categorie);
        }
        if (s.getPrix_max() != 0) {
            Predicate max = builder.lessThanOrEqualTo(lot.get("prixMinimal"), s.getPrix_max());
            criteria = builder.and(criteria, max);
        }
        if (s.getPrix_min() != 0) {
            Predicate min = builder.greaterThanOrEqualTo(lot.get("prixMinimal"), s.getPrix_min());
            criteria = builder.and(criteria, min);
        }
        if (s.isStatut() == true) {
            Predicate statut = builder.equal(lot.get("statut"), s.isStatut());
            criteria = builder.and(criteria, statut);
        }
        query.select(lot).where(criteria);
        return entityManager.createQuery(query).getResultList();
       
    }

    public List<Lot> findById(int id) {
        return this.lotRepo.findById(id);
    }

    public List<Lot> myEnchere(int id) {
        return this.lotRepo.myEnchere(id);
    }

    public  void insertEnchere(Lot lot) {
        lotRepo.insertEnchere(lot.getDateDebut(), lot.getDure(), lot.getPrixMinimal(),
                lot.getUtilisateur(), lot.getNom(), lot.getDescri(), lot.isStatut(), lot.getCategorie());
    }


}
