package com.example.savingmoney.data.repository

import com.example.savingmoney.data.model.Plan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    private val userId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    fun getAllPlans(): Flow<List<Plan>> = callbackFlow {
        val uid = userId ?: return@callbackFlow
        val collection = db.collection("users").document(uid).collection("plans")

        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val plans = snapshot?.documents?.mapNotNull { it.toObject(Plan::class.java) } ?: emptyList()
            trySend(plans)
        }

        awaitClose { listener.remove() }
    }

    fun addPlan(plan: Plan) {
        val uid = userId ?: return
        val planId = if (plan.id.isBlank()) db.collection("users").document(uid)
            .collection("plans").document().id else plan.id
        val planWithId = plan.copy(id = planId)

        db.collection("users").document(uid)
            .collection("plans")
            .document(planId)
            .set(planWithId)
    }


    fun updatePlan(plan: Plan) {
        val uid = userId ?: return
        db.collection("users").document(uid)
            .collection("plans").document(plan.id)
            .set(plan)
    }

    fun deletePlan(planId: String) {
        val uid = userId ?: return
        db.collection("users").document(uid)
            .collection("plans").document(planId)
            .delete()
    }
}
