import React,{useState,createContext, useContext} from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBell,faChevronLeft,faExclamationTriangle } from '@fortawesome/free-solid-svg-icons'
import axios from 'axios'

const notifContext = createContext();
    
function Circle(){

    const {nbrNotifs,setNotifShown} = useContext(notifContext)

    return (
        <div className="circle" onClick={()=>{setNotifShown(prev=>!prev)}}>
            <FontAwesomeIcon icon={faBell}/>
            <div className="red-circle">
                <p>{nbrNotifs}</p>
            </div>
        </div>
    )
}

function Notif({problem}){

    const [shown, setShown] = useState(false)

    return (
        <>
            <div className="problem" onClick={()=>{setShown(!shown)}}>
                <h3> <FontAwesomeIcon icon={faExclamationTriangle} className="icon"/> {problem.title} </h3>
                <p> {problem.location} </p>
                <div className={"description "+ (shown?"visible":"")}>
                    {problem.description}
                </div>
                <FontAwesomeIcon icon={faChevronLeft} className={"arrow "+(shown?"bottom":"")}/>
            </div>
            <hr />
        </>
    )
}

function NotifsContainer(){

    const {notifShown,problem_table} = useContext(notifContext)

    return (
        <div className={"notifications "+(notifShown?"visible":"")}>
            {problem_table.map(problem=>(
                <Notif problem={problem}/>
            ))}
        </div>
    )
}

export default function Notifications() {
    const [problemTable, setProblemTable] = useState([])
    const [nbrNotifs, setNbrNotifs] = useState(problem_table.length)
    const [notifShown, setNotifShown] = useState(true)

    const fetchNotif =async ()=>{

        const userData = JSON.parse(localStorage.getItem('userData'));

        await axios.get("http://localhost:8080/Heating/getNews",{params:{
            country:userData.country,
            city:userData.state, 
            email:userData.email
        }}).then(response=>{
            setProblemTable(response.data);
        })
    }

    useEffect(async()=>{
        await fetchNotif(); 
        setInterval(async()=>{
            await fetchNotif();
        },60000)
    },[])


    const value ={
        notifShown , setNotifShown,
        nbrNotifs, setNbrNotifs,
        problem_table:problemTable
    }

    return (
        <notifContext.Provider value={value}>
            <Circle/>
            <NotifsContainer/>
        </notifContext.Provider>
    )
}
