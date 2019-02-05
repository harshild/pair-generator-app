package sdc.vw.com.pairgenerator


class TeamDataProvider() {

    val employeeList: HashMap<String, List<String>>

    init {
        employeeList = HashMap()
        employeeList.set(
                "ASID",
                listOf("Harshil", "Saurabh", "Amit Sharma", "Rolly", "Amit Hule", "Sagar")
        )
        employeeList.set(
                "SDB", listOf("Asish", "Sumit", "Abhinav", "vaibhav", "Amar", "Amey", "sham", "Vinit")
        )
        employeeList.set(
                "Platform", listOf("Shoyeb", "Manoj", "Chirag", "Yogita")
        )
        employeeList.set(
                "Designer", listOf("Pankaj")
        )
    }

    fun getListOfProjectName(): List<String> {
        return employeeList.keys.toList()
    }

    fun getListOfMembers(projectName: String): List<String> {
        return employeeList.get(projectName).orEmpty()
    }

}
