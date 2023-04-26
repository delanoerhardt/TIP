op      = +
        | -
        | *
        | /
        | >
        | ==

exp     = basic
        | exp op exp
        | exp ( exp , . . ., exp )
        | exp . id

# post transform
exp     = basic exp'
exp'    = op exp 'exp
        | (exp, ... , exp) 'exp
        | . id 'exp
        | END

basic   = input
        | alloc exp
        | & id
        | * exp
        | null
        | id ( exp,. . .,exp )
        | id
        | int
        | { id : exp , . . ., id : exp }
        | ( exp )

stm     = * exp = exp;
        | ( * exp ) . id = exp;
        | output exp ;
        | if ( exp ) { stm } [[ else { stm } ]?]
        | while ( exp ) { stm }
        | id . id = exp ;
        | id = exp ;
        | stm stm
        |

fun     = id ( id , ..., id ) {
                [[ var id , ..., id ; ]?]
                stm
                return exp;
        }

prog    = fun ... fun
