<template>
  <!-- easy components usage, already shipped with bootstrap css-->
  <div>
    <b-container>
      <!-- try to remove :fields=”fields” to see the magic -->
      <b-table striped over :items="subjects" :fields="fields">
        <template v-slot:cell(actions)="row">
          <nuxt-link class="btn btn-link" :to="`/subjects/${row.item.code}`"
            >Details</nuxt-link
          >
        </template>
      </b-table>
      <nuxt-link to="/create">Create a New Subject</nuxt-link>
      <br />
      <nuxt-link to="/">Back</nuxt-link>
    </b-container>
  </div>
</template>
<script>
export default {
  data() {
    return {
      fields: [
        "code",
        "name",
        "course",
        "courseYear",
        "scholarYear",
        "students",
        "teachers",
        "actions",
      ],
      subjects: [],
    };
  },
  created() {
    this.$axios.$get("/api/subjects/all").then((subjects) => {
      this.subjects = subjects;
    });
  },
};
</script>
<style></style>
